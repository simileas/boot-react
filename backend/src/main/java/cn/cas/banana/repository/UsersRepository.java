package cn.cas.banana.repository;

import cn.cas.banana.entity.users.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String username);

  Long countByUsernameLike(String username);

  Page<UserEntity> findByUsernameLike(String username, Pageable pageable);
}
