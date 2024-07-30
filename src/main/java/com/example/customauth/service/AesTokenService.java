package com.example.customauth.service;

import com.example.customauth.domain.Token;
import com.example.customauth.util.AesEncryptionUtil;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AesTokenService {

  private final long tokenValidityMinutes;
  private final AesEncryptionUtil aesEncryptionUtil;

  public AesTokenService(@Value("${token.validity.minutes}")
                       long tokenValidityMinutes,
                         AesEncryptionUtil aesEncryptionUtil) {
    this.tokenValidityMinutes = tokenValidityMinutes;
    this.aesEncryptionUtil = aesEncryptionUtil;
  }


  public String generateToken(String mobileNumber) throws Exception {
    Token token = new Token(mobileNumber);
    String tokenString = Token.serializeToken(token);
    return aesEncryptionUtil.encrypt(tokenString);
  }

  public boolean validateToken(String encryptedToken) throws Exception {
    String decryptedToken = aesEncryptionUtil.decrypt(encryptedToken);
    Token token = Token.deserialize(decryptedToken);

    return token != null &&
        token.getCreationTime().plusMillis(TimeUnit.MINUTES.toMillis(tokenValidityMinutes))
            .isAfter(Instant.now());
  }

}

