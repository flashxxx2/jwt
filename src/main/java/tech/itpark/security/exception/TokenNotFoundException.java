package tech.itpark.security.exception;

import javax.security.sasl.AuthenticationException;

public class TokenNotFoundException extends AuthenticationException {
  public TokenNotFoundException() {
  }

  public TokenNotFoundException(String detail) {
    super(detail);
  }

  public TokenNotFoundException(String detail, Throwable ex) {
    super(detail, ex);
  }
}
