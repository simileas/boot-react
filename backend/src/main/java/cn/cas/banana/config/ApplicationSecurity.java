package cn.cas.banana.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
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
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

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
        .authenticationEntryPoint((request, response, authException) -> {
          if (authException instanceof DisabledException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
          } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
          }
        })
        .and()
        .rememberMe()
        .userDetailsService(userDetailsService)
        .tokenRepository(persistentTokenRepository)
        .and()
        .logout()
        .logoutUrl("/api/user/logout")
        .addLogoutHandler(new SecurityContextLogoutHandler())
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        .and().csrf().disable()
        .headers().contentTypeOptions().disable().frameOptions().disable();
  }

  /**
   * UserDetailsService???JdbcUserDetailsManager ?????? Bean.
   *
   * @param dataSource UserDetailsService data source
   * @return JdbcUserDetailsManager instance
   */
  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) throws Exception {
    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
    jdbcUserDetailsManager.setDataSource(dataSource);
    return jdbcUserDetailsManager;
  }

  /**
   * PersistentTokenRepository ?????? Bean.
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
   * ?????? PasswordEncoder Bean.
   *
   * @return PasswordEncoder ??????????????????
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }

  /**
   * ?????? SessionRegistry Bean.
   *
   * @return SessionInformation instances
   */
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
