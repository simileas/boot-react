package cn.cas.banana.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

  @Autowired
  private PersistentTokenRepository persistentTokenRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/api/**")
        .authenticated()
        .anyRequest()
        .permitAll()
        .and()
        .httpBasic()
        .realmName("common-dev")
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .rememberMe()
        .userDetailsService(userDetailsService)
        .tokenRepository(persistentTokenRepository)
        .and()
        .logout()
        .logoutUrl("/api/user/logout")
        .addLogoutHandler(new SecurityContextLogoutHandler())
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        .and()
        .csrf()
        .disable()
        .headers().contentTypeOptions().disable();
  }

  /**
   * UserDetailsService、JdbcUserDetailsManager 缺省 Bean.
   *
   * @param dataSource UserDetailsService data source
   * @return JdbcUserDetailsManager instance
   */
  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
    jdbcUserDetailsManager.setDataSource(dataSource);
    return jdbcUserDetailsManager;
  }

  /**
   * PersistentTokenRepository 缺省 Bean.
   *
   * @param dataSource data source
   * @return JdbcTokenRepositoryImpl instance
   */
  @Bean
  public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    jdbcTokenRepository.setDataSource(dataSource);
    return jdbcTokenRepository;
  }

  /**
   * 默认 PasswordEncoder Bean.
   *
   * @return PasswordEncoder 密码加密方式
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }

  /**
   * 默认 SessionRegistry Bean.
   *
   * @return SessionInformation instances
   */
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
