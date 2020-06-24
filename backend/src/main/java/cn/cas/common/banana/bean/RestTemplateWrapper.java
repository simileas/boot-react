package cn.cas.common.banana.bean;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateWrapper extends RestTemplate {

  public RestTemplateWrapper(ClientHttpRequestFactory factory) {
    super(factory);
  }
}
