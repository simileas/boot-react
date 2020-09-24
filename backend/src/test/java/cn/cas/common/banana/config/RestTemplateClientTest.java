package cn.cas.common.banana.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Slf4j
class RestTemplateClientTest {

  @Autowired
  private RestTemplate restTemplate;
}
