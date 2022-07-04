package tech.itpark.security.service;

import tech.itpark.security.domain.User;
import tech.itpark.security.exception.TokenNotFoundException;

// TODO: -> replace with AuthenticationProvider
public interface UserService {
  User findByToken(String token) throws TokenNotFoundException;
}
