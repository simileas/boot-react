package cn.cas.common.banana.config;

import cn.cas.common.banana.bean.RestTemplateWrapper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestTemplateClient {

  @Value("${http.proxy.host}")
  private String httpProxyHost;

  @Value("${http.proxy.port}")
  private int httpProxyPort;

  @Bean
  public RestTemplateWrapper restTemplateWrapper() {
    final HttpComponentsClientHttpRequestFactory requestFactory =
      new HttpComponentsClientHttpRequestFactory();
    CloseableHttpClient client =
      HttpClientBuilder.create().disableRedirectHandling().setRedirectStrategy(
        new LaxRedirectStrategy() {
          @Override
          public boolean isRedirected(
            HttpRequest request, HttpResponse response, HttpContext context) {
            return false;
          }
        }
      ).build();
    requestFactory.setHttpClient(client);
    return new RestTemplateWrapper(requestFactory);
  }
}
