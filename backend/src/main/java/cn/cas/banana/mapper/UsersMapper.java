package cn.cas.banana.mapper;

import cn.cas.banana.entity.PaginationParam;
import cn.cas.banana.entity.users.UserEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

  int count();

  List<UserEntity> findAll(PaginationParam param);

  UserEntity findByUsername(String username);
}
