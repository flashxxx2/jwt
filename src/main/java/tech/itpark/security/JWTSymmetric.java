package tech.itpark.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class JWTSymmetric {
  public static void main(String[] args) throws JOSEException, ParseException {
    // client -> claims
    // exp
    // iat
    final var claims = new JWTClaimsSet.Builder()
        .expirationTime(Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
        .issueTime(new Date())
        .claim("id", 1L)
        .claim("username", "admin")
        .build();

    System.out.println(claims.toString(true));

    // Подпись
    final var signed = new SignedJWT(
        new JWSHeader(JWSAlgorithm.HS256),
        claims
    );

    final var random = new SecureRandom();
    // shared key - secret
    final var secret = new byte[32];
    random.nextBytes(secret);
    final var signer = new MACSigner(secret);
    signed.sign(signer);

    final var serialized = signed.serialize();
    System.out.println(serialized);

    // verification
    final var deserialized = SignedJWT.parse(serialized);
    final var verifier = new MACVerifier(secret);
    final var verified = deserialized.verify(verifier);
    System.out.println(verified);
    // TODO: + check expire time
  }
}
