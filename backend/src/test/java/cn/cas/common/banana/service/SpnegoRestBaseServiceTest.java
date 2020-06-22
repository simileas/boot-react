package cn.cas.common.banana.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SpnegoRestBaseServiceTest {

  @Autowired
  ModelService modelService;

  @Test
  void unzip() throws IOException, URISyntaxException {
    String zipPath = System.getProperty("user.home")
      + "/workspace/data/spark-files.zip";
    List<String> filenames =
      modelService
        .unzip(new ZipInputStream(new FileInputStream(zipPath)));
    log.info("{}", filenames);
  }
}
