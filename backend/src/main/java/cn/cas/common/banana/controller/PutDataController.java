package cn.cas.common.banana.controller;

import cn.cas.common.banana.entity.Message;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/put")
@Slf4j
@ResponseBody
public class PutDataController {

  @PutMapping()
  public Message index(HttpServletRequest request) {
    log.info("{}", request.getContentLength());
    return Message.builder().message("ok").build();
  }
}
