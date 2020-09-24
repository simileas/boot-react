package cn.cas.common.banana.controller;

import cn.cas.common.banana.entity.Message;
import cn.cas.common.banana.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/user")
@ResponseBody
@Slf4j
public class UserController {

  @GetMapping("/current")
  public Message current() {
    UserEntity user = UserEntity.builder().id(1L).build();
    return Message.builder().message("").object(user).build();
  }
}
