package cn.cas.common.banana.service;

import cn.cas.common.banana.entity.Header;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

@Component
@Slf4j
public class ModelService extends SpnegoRestBaseService {

  public List<String> unzip(ZipInputStream zipInputStream) throws IOException, URISyntaxException {
    final String commonBase = "/user/" + USERNAME + "/oozie_tmp_dir";
    UUID uuid = UUID.randomUUID();
    String randomDirectory = DigestUtils.md5Hex(uuid.toString());
    final String tempBase = commonBase + "/" + randomDirectory;

    URI uri = new URI(webhdfsUrl);
    List<String> fileList = new ArrayList<>();
    ZipEntry ze;
    while ((ze = zipInputStream.getNextEntry()) != null) {
      log.info("Process: {}", ze.getName());
      if (ze.isDirectory()) {
        createDirectory(uri, tempBase, ze);
      } else {
        fileList.add(writeFile(uri, tempBase, ze, zipInputStream));
      }
    }
    return fileList;
  }

  private void createDirectory(URI uri, String tempBase, ZipEntry ze) {
    HttpHeaders authHeader = getAuthHeader(uri.getHost());
    HttpEntity<String> request = new HttpEntity<String>(null, authHeader);
    String url = webhdfsUrl + tempBase + "/" + ze.getName() + "?op=MKDIRS";
    ResponseEntity<String> mkdirResponse = restTemplate
      .exchange(url, HttpMethod.PUT, request, String.class);
    assert mkdirResponse.getStatusCode().is2xxSuccessful();
  }

  private String writeFile(URI uri,
                           String tempBase,
                           ZipEntry ze, ZipInputStream zipInputStream) throws IOException {
    HttpHeaders authHeader = getAuthHeader(uri.getHost());
    HttpEntity<String> request = new HttpEntity<String>(null, authHeader);
    String url = webhdfsUrl + tempBase + "/" + ze.getName() + "?op=CREATE";
    ResponseEntity<String> createResponse = restTemplate
      .exchange(url, HttpMethod.PUT, request, String.class);
    URI location = createResponse.getHeaders().getLocation();
    assert location != null;
    byte[] bytes = IOUtils.toByteArray(zipInputStream);
    HttpEntity<ByteArrayResource> uploadEntity = new HttpEntity<>(
      new ByteArrayResource(bytes)
    );
    zipInputStream.closeEntry();

    ResponseEntity<String> uploadResponse =
      restTemplate.exchange(location, HttpMethod.PUT, uploadEntity, String.class);
    assert uploadResponse.getStatusCode().is2xxSuccessful();
    return "hdfs://" + tempBase + "/" + ze.getName();
  }

  private HttpHeaders getAuthHeader(String host) {
    ResponseEntity<Header> headerResponseEntity = restTemplate
      .getForEntity(tokenServiceUrl + "/api/token/" + host, Header.class);
    Header header = headerResponseEntity.getBody();
    assert header != null;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set(header.getKey(), header.getValue());
    return httpHeaders;
  }
}
