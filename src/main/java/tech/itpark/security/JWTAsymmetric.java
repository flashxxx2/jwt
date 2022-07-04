package tech.itpark.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

// https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide
public class JWTAsymmetric {
  public static void main(String[] args) throws JOSEException, ParseException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    /*
    final var key = new RSAKeyGenerator(2048)
        .generate();
    final var publicKey = key.toRSAPublicKey();
    final var privateKey = key.toRSAPrivateKey();

    Files.write(Path.of("public.key"), Base64.getMimeEncoder().encode(publicKey.getEncoded()));
    Files.write(Path.of("private.key"), Base64.getMimeEncoder().encode(privateKey.getEncoded()));
    */

    final var keyFactory = KeyFactory.getInstance("RSA");
    final var publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getMimeDecoder().decode(Files.readString(Path.of("public.key"))
        .replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
    )));
    final var privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(Files.readString(Path.of("private.key"))
        .replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
    )));

    final var claims = new JWTClaimsSet.Builder()
        .expirationTime(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
        .issueTime(new Date())
        .claim("id", 1L)
        .claim("username", "admin")
        .build();

    final var signer = new RSASSASigner(privateKey);
    final var signed = new SignedJWT(
        new JWSHeader(JWSAlgorithm.RS256),
        claims
    );
    signed.sign(signer);

    final var serialized = signed.serialize();
    System.out.println(serialized);

    final var deserialized = SignedJWT.parse(serialized);
    System.out.println(deserialized.getPayload());
    final var verifier = new RSASSAVerifier(publicKey);
    final var verified = deserialized.verify(verifier);
    System.out.println(verified);
  }
}
