package cn.cas.banana.controller;

import cn.cas.banana.entity.Message;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionController {

  @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
  ResponseEntity<Message> constraintViolation(
      SQLIntegrityConstraintViolationException exception) {
    return new ResponseEntity<>(
        Message.builder()
            .message(exception.getMessage()).build(),
        HttpStatus.BAD_REQUEST);
  }
}
