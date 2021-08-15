package cn.cas.banana.entity.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserDto {

  private String username;
  private String password;
  private String role;
  private boolean enabled;
}
