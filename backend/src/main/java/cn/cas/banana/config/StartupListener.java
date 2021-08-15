package cn.cas.banana.config;

import cn.cas.banana.entity.users.UserEntity;
import cn.cas.banana.mapper.UsersMapper;
import cn.cas.banana.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private UsersService usersService;

  /**
   * start routine.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void run() {
    UserEntity admin = usersMapper.findByUsername("admin");
    if (admin == null) {
      usersService.createDefaultUser();
    }
  }
}
