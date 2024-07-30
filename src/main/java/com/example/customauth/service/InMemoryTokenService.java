package com.example.customauth.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class InMemoryTokenService {

  private final ConcurrentMap<String, String> tokenStore = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Long> tokenExpiryStore = new ConcurrentHashMap<>();
  private static final long TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(10);
  private final SecureRandom secureRandom = new SecureRandom();

  public String generateToken(String mobileNumber) {
    String token = generateUniqueToken();
    long expiryTime = System.currentTimeMillis() + TOKEN_VALIDITY;
    tokenStore.put(mobileNumber, token);
    tokenExpiryStore.put(token, expiryTime);
    return token;
  }

  public boolean validateToken(String token) {
    Long expiryTime = tokenExpiryStore.get(token);
    return expiryTime != null && System.currentTimeMillis() < expiryTime;
  }

  private String generateUniqueToken() {
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }
}
