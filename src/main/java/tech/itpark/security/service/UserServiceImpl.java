package tech.itpark.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.itpark.security.domain.User;
import tech.itpark.security.entity.TokenEntity;
import tech.itpark.security.entity.UserEntity;
import tech.itpark.security.exception.TokenNotFoundException;
import tech.itpark.security.repository.TokenRepository;
import tech.itpark.security.repository.UserRepository;
import tech.itpark.security.security.TokenGenerator;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenGenerator tokenGenerator;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

  @Override
  public User findByToken(String token) throws TokenNotFoundException {
    return tokenRepository.findById(token)
        .map(TokenEntity::getUser)
        .map(UserEntity::toUser)
        .orElseThrow(TokenNotFoundException::new);
  }

  public String create(String name, String username, String password) {
    return create(name, username, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
  }

  // backend:
  // 1. Удобные (не ленивые) <- token
  // 2. Неудобные (ленивые): 201
  public String create(String name, String username, String password, Collection<GrantedAuthority> authorities) {
    final var savedUser = userRepository.save(
        new UserEntity(
            0L,
            name,
            username,
            passwordEncoder.encode(password),
            authorities,
            true,
            true,
            true,
            true
        )
    );
    final var token = tokenGenerator.generateToken();
    tokenRepository.save(new TokenEntity(token, savedUser));
    return token;
  }
}
