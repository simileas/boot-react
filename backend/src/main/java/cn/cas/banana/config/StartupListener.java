package cn.cas.banana.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

  /** start routine. */
  @EventListener(ApplicationReadyEvent.class)
  public void run() {}
}
