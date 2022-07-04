package tech.itpark.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Data
public class User implements UserDetails {
  private long id;
  private String name;
  private String username;
  private String password;
  private Collection<GrantedAuthority> authorities;
  boolean accountNonExpired;
  boolean accountNonLocked;
  boolean credentialsNonExpired;
  boolean enabled;
}
