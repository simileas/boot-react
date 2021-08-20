package cn.cas.banana.controller;

import cn.cas.banana.entity.Message;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/user")
@ResponseBody
@Slf4j
public class UserController {

  private static final String CURRENT_USER_KEY = "CURRENT_USER";

  @Autowired
  private JdbcUserDetailsManager jdbcUserDetailsManager;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/current")
  Message current(HttpSession httpSession) {
    SecurityContext context =
        (SecurityContext)
            httpSession.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    User current = (User) context.getAuthentication().getPrincipal();
    return Message.builder().message("").object(current).build();
  }

  @GetMapping("/login")
  Message login(HttpSession httpSession) {
    SecurityContext context =
        (SecurityContext)
            httpSession.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    WebAuthenticationDetails details =
        (WebAuthenticationDetails) context.getAuthentication().getDetails();
    User loggedIn = (User) context.getAuthentication().getPrincipal();
    log.info("{} LOGIN IP: {}", loggedIn.getUsername(), details.getRemoteAddress());
    httpSession.setAttribute(CURRENT_USER_KEY, loggedIn);
    return Message.builder().message("Login successfully.").build();
  }

  @PostMapping("/set-password")
  ResponseEntity<Message> setPassword(String oldPassword, String newPassword) {
    Authentication currentUser = SecurityContextHolder.getContext()
        .getAuthentication();
    String username = currentUser.getName();
    try {
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
    } catch (AuthenticationException bce) {
      return ResponseEntity.badRequest()
          .body(Message.builder().message(bce.getMessage()).build());
    }
    jdbcUserDetailsManager.changePassword(oldPassword, passwordEncoder.encode(newPassword));
    return ResponseEntity.ok().body(Message.builder().build());
  }
}
