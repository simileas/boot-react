package cn.cas.common.banana.controller;

import cn.cas.common.banana.entity.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/model")
@ResponseBody
public class ModelController {

  @PostMapping("/upload-zip")
  public Message upload(@RequestParam MultipartFile zipFile) {

    return Message.builder().build();
  }
}
