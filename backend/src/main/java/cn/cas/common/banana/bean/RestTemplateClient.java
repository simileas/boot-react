package cn.cas.common.banana.bean;

import java.net.InetSocketAddress;
import java.net.Proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {

  @Value("${socks.proxy.host:-}")
  private String socksProxyHost;

  @Value("${socks.proxy.port:-1}")
  private int socksProxyPort;

  /**
   * 生成 RestTemplate 的 bean，依赖注入的模式.
   *
   * @return RestTemplate instance
   */
  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    if (!"-".equals(socksProxyHost)) {
      requestFactory.setProxy(new Proxy(Proxy.Type.SOCKS,
          new InetSocketAddress(socksProxyHost, socksProxyPort)));
    }
    return new RestTemplate(requestFactory);
  }
}
