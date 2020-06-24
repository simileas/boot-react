package cn.cas.common.banana.service;

import cn.cas.common.banana.bean.RestTemplateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class SpnegoRestBaseService {

  protected static final String USERNAME = "admin";

  // http://ux1.in.nopadding.com:50070/webhdfs/v1
  @Value("${webhdfs.url}")
  protected String webhdfsUrl;

  // http://192.168.1.23:8200
  @Value("${token.service.url}")
  protected String tokenServiceUrl;

  @Autowired
  RestTemplateWrapper restTemplate;
}
