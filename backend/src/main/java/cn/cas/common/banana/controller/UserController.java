package cn.cas.common.banana.controller;

import cn.cas.common.banana.entity.Message;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/user")
@ResponseBody
@Slf4j
public class UserController {

  public static final String SESSION_CURRENT_KEY = "current";

  @GetMapping("/current")
  Message current(HttpSession httpSession) {
    UserDetails userDetails = (UserDetails) httpSession.getAttribute(SESSION_CURRENT_KEY);
    return Message.builder().message("").object(userDetails).build();
  }

  @GetMapping("/login")
  Message login(HttpSession httpSession) {
    SecurityContext context =
        (SecurityContext)
            httpSession.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    WebAuthenticationDetails details =
        (WebAuthenticationDetails) context.getAuthentication().getDetails();
    UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
    log.info("user {} logged in: ip {}", userDetails.getUsername(), details.getRemoteAddress());
    httpSession.setAttribute(SESSION_CURRENT_KEY, userDetails);
    return Message.builder().message("LOGIN").build();
  }
}
