package cn.cas.banana.controller;

import cn.cas.banana.entity.Message;
import cn.cas.banana.entity.PaginationObject;
import cn.cas.banana.entity.PaginationParam;
import cn.cas.banana.entity.users.UserDto;
import cn.cas.banana.entity.users.UserEntity;
import cn.cas.banana.mapper.UsersMapper;
import cn.cas.banana.service.UsersService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/users")
@ResponseBody
@Slf4j
public class UsersController {

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private UsersService usersService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JdbcUserDetailsManager jdbcUserDetailsManager;

  @GetMapping
  PaginationObject index(Integer current, Integer pageSize) {
    int total = usersMapper.count();
    List<UserEntity> users = usersMapper.findAll(PaginationParam.builder()
        .current(current == null ? 1 : current)
        .pageSize(pageSize == null ? 10 : pageSize).build());
    return PaginationObject.builder().total(total).list(users).build();
  }

  @PutMapping
  Message create(@RequestBody UserDto user) {
    jdbcUserDetailsManager.createUser(User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole())
        .passwordEncoder(password -> passwordEncoder.encode(password))
        .build());
    return Message.builder().build();
  }

  @DeleteMapping("/{username}")
  Message delete(@PathVariable String username) {
    jdbcUserDetailsManager.deleteUser(username);
    return Message.builder().build();
  }

  @PostMapping("/state")
  Message updateState(UserDto userDto) {
    UserEntity userEntity = usersMapper.findByUsername(userDto.getUsername());
    jdbcUserDetailsManager.updateUser(User.builder()
        .username(userDto.getUsername())
        .password(userEntity.getPassword())
        .roles(userEntity.getAuthorities().get(0).getAuthority())
        .disabled(!(userDto.isEnabled()))
        .passwordEncoder(password -> password).build());
    return Message.builder().build();
  }

  @PostMapping("/password")
  Message updatePassword(UserDto userDto) {
    UserEntity userEntity = usersMapper.findByUsername(userDto.getUsername());
    jdbcUserDetailsManager.updateUser(User.builder()
        .username(userDto.getUsername())
        .password(userDto.getPassword())
        .roles(userEntity.getAuthorities().get(0).getAuthority())
        .disabled(!(userEntity.isEnabled()))
        .passwordEncoder(password -> passwordEncoder.encode(password)).build());
    return Message.builder().build();
  }

  @PostMapping("/role")
  Message updateRole(UserDto userDto) {
    UserEntity userEntity = usersMapper.findByUsername(userDto.getUsername());
    jdbcUserDetailsManager.updateUser(User.builder()
        .username(userDto.getUsername())
        .password(userEntity.getPassword())
        .roles(userDto.getRole())
        .disabled(!(userEntity.isEnabled()))
        .passwordEncoder(password -> passwordEncoder.encode(password)).build());
    return Message.builder().build();
  }
}
