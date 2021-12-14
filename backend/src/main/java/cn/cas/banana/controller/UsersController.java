package cn.cas.banana.controller;

import cn.cas.banana.entity.Message;
import cn.cas.banana.entity.PaginationObject;
import cn.cas.banana.entity.users.UserDto;
import cn.cas.banana.entity.users.UserEntity;
import cn.cas.banana.repository.UsersRepository;
import cn.cas.banana.service.UsersService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  @Autowired private UsersRepository userRepository;

  @Autowired private UsersService usersService;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JdbcUserDetailsManager jdbcUserDetailsManager;

  @GetMapping
  PaginationObject index(String username, Integer current, Integer pageSize) {
    Page<UserEntity> users = userRepository.findByUsernameLike("%" + username + "%", PageRequest.of(
        current == null ? 0 : current - 1, pageSize == null ? 10 : pageSize,
        Sort.by(Sort.Order.desc("id"))));
    return PaginationObject.builder().total(users.getTotalElements())
        .list(users.getContent()).build();
  }

  @PutMapping
  Message create(@RequestBody UserDto user) {
    jdbcUserDetailsManager.createUser(
        User.builder()
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
    Optional<UserEntity> userEntity = userRepository.findByUsername(userDto.getUsername());
    userEntity.ifPresent(
        entity ->
            jdbcUserDetailsManager.updateUser(
                User.builder()
                    .username(userDto.getUsername())
                    .password(entity.getPassword())
                    .roles(entity.getAuthorities().get(0).getAuthority().substring(5))
                    .disabled(!(userDto.isEnabled()))
                    .passwordEncoder(password -> password)
                    .build()));
    return Message.builder().build();
  }

  @PostMapping("/password")
  Message updatePassword(UserDto userDto) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(userDto.getUsername());
    userEntity.ifPresent(
        entity ->
            jdbcUserDetailsManager.updateUser(
                User.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .roles(entity.getAuthorities().get(0).getAuthority().substring(5))
                    .disabled(!(entity.isEnabled()))
                    .passwordEncoder(password -> passwordEncoder.encode(password))
                    .build()));
    return Message.builder().build();
  }

  @PostMapping("/role")
  Message updateRole(UserDto userDto) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(userDto.getUsername());
    userEntity.ifPresent(
        entity ->
            jdbcUserDetailsManager.updateUser(
                User.builder()
                    .username(userDto.getUsername())
                    .password(entity.getPassword())
                    .roles(userDto.getRole())
                    .disabled(!(entity.isEnabled()))
                    .passwordEncoder(password -> passwordEncoder.encode(password))
                    .build()));
    return Message.builder().build();
  }
}
