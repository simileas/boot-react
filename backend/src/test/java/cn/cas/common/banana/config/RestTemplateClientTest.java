package cn.cas.common.banana.config;

import cn.cas.common.banana.bean.RestTemplateWrapper;
import cn.cas.common.banana.entity.Header;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@Slf4j
class RestTemplateClientTest {

  @Autowired
  private RestTemplateWrapper restTemplate;

  // http://192.168.1.23:8200
  @Value("${token.service.url}")
  protected String tokenServiceUrl;

  @Test
  void restTemplateWrapper() {
    HttpHeaders authHeader = getAuthHeader("ux4.in.nopadding.com");
    HttpEntity<String> request = new HttpEntity<String>(null, authHeader);
    ResponseEntity<String> response = restTemplate.
      exchange(
        "http://ux4.in.nopadding.com:8042" +
          "/node/containerlogs/container_1593608192415_0032_02_000001/admin",
        HttpMethod.GET,
        request,
        String.class);
    URI uri = response.getHeaders().getLocation();
    assert uri != null;
    log.info(uri.toString());
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
