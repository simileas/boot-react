package cn.cas.banana.entity.users;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

  private Long id;
  private String username;

  private String password;
  private boolean enabled;
  private List<AuthorityEntity> authorities;
}
