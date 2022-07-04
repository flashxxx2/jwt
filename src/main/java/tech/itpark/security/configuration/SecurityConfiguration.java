package tech.itpark.security.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tech.itpark.security.domain.User;
import tech.itpark.security.filter.XTokenFilter;
import tech.itpark.security.security.TokenGenerator;
import tech.itpark.security.service.UserService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Setter(onMethod_ = {@Autowired})
  private UserService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .logout().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterAfter(new XTokenFilter(userService), BasicAuthenticationFilter.class)
        .anonymous(configurer -> configurer.principal(new User(
            -1,
            "anonymous",
            "anonymous",
            "***",
            List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")),
            true,
            true,
            true,
            true
        )).authorities(List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))))
        .authorizeRequests()
        // TODO: check
        .antMatchers(HttpMethod.POST, "/api/users/register", "/api/users/restore").anonymous()
        .antMatchers(HttpMethod.POST, "/api/users/register/confirm", "/api/users/restore/confirm").anonymous()
        .antMatchers("/api/**").authenticated()
    ;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public TokenGenerator tokenGenerator() {
    return new TokenGenerator(new Random(0), 64);
  }
}
