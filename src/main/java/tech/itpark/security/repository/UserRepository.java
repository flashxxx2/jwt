package tech.itpark.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.itpark.security.entity.UserEntity;

import java.util.Optional;

// SimpleJpaRepository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}
