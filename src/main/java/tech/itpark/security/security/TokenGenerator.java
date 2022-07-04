package tech.itpark.security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.codec.Hex;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
public class TokenGenerator {
  private final Random random;
  private final int keyLength;

  public TokenGenerator(int keyLength) {
    this.random = new SecureRandom();
    this.keyLength = keyLength;
  }

  public String generateToken() {
    byte[] bytes = new byte[keyLength];
    random.nextBytes(bytes);
    return new String(Hex.encode(bytes));
  }
}
