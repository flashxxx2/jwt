package tech.itpark.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.itpark.security.domain.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String password;
  @ElementCollection(fetch = FetchType.EAGER)
  // Authority -> Enum -> String
  private Collection<GrantedAuthority> authorities = Collections.emptyList();
  boolean accountNonExpired;
  boolean accountNonLocked;
  boolean credentialsNonExpired;
  boolean enabled;

  public User toUser() {
    return new User(
        id, name, username, "***", authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled
    );
  }
}
