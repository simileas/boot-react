package cn.cas.banana.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object object;
}
