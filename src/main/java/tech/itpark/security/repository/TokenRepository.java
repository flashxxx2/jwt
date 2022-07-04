package tech.itpark.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.itpark.security.entity.TokenEntity;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
}
