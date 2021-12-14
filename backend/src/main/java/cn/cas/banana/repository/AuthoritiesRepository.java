package cn.cas.banana.repository;

import cn.cas.banana.entity.users.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<AuthorityEntity, Integer> {

}
