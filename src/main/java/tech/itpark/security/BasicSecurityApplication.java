package tech.itpark.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.itpark.security.service.UserServiceImpl;

import java.util.List;

@SpringBootApplication
public class BasicSecurityApplication {
  private final Log logger = LogFactory.getLog(getClass());

  public static void main(String[] args) {
    SpringApplication.run(BasicSecurityApplication.class, args);
  }

  @Bean
  public CommandLineRunner runner(UserServiceImpl service) {
     return args -> {
       String token = service.create("admin", "admin", "password", List.of(
           // Authority - без префикса
           // Role - ROLE_
           new SimpleGrantedAuthority("ROLE_USER"),
           new SimpleGrantedAuthority("ROLE_ADMIN")
       ));
       logger.info(String.format("Token: %s", token));
     };
  }

}
