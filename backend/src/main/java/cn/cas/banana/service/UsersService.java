package cn.cas.banana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

  @Autowired
  private JdbcUserDetailsManager jdbcUserDetailsManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 创建用户.
   */
  public void createDefaultUser() {
    UserDetails user =
        User.builder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .passwordEncoder(password -> passwordEncoder.encode(password))
            .build();
    jdbcUserDetailsManager.createUser(user);
  }
}
