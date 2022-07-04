package tech.itpark.security.controller;

import org.springframework.web.bind.annotation.*;
import tech.itpark.security.dto.UserRegistrationRequestDto;
import tech.itpark.security.dto.UserResponseDto;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
  @GetMapping
  public List<UserResponseDto> getAll() {
    return Collections.emptyList();
  }

  @PostMapping("/register")
  public void register(@RequestBody UserRegistrationRequestDto dto) {

  }
}
