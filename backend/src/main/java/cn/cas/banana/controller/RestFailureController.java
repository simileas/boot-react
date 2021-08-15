package cn.cas.banana.controller;

import cn.cas.banana.entity.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
public class RestFailureController implements ErrorController {

  private static final String ERROR_PATH = "/error";

  /**
   * Request handle.
   *
   * @param request Servlet request bean
   * @return Json message
   */
  @RequestMapping(value = ERROR_PATH)
  public Message handleError(HttpServletRequest request) {
    String stackTrace = "";
    Throwable ex = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (ex != null) {
      stackTrace = ExceptionUtils.getStackTrace(ex);
    }
    return Message.builder()
        .message(request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString())
        .object(stackTrace)
        .build();
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }
}
