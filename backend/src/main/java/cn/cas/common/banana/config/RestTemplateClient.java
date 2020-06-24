package cn.cas.common.banana.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {

  @Value("${socks.proxy.host}")
  private String socksProxyHost;

  @Value("${socks.proxy.port}")
  private int socksProxyPort;

  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    Proxy proxy = new Proxy(Proxy.Type.SOCKS,
      new InetSocketAddress(socksProxyHost, socksProxyPort));
    requestFactory.setProxy(proxy);
    return new RestTemplate(requestFactory);
  }
}
