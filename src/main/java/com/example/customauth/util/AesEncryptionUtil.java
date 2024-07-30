package com.example.customauth.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptionUtil {

  private static final String AES = "AES";
  private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128;
  private static final int GCM_IV_LENGTH = 12;

  private final SecretKey secretKey;

  public AesEncryptionUtil(@Value("${token.encryption.key}") String encryptionKey) {
    this.secretKey = getSecretKey(encryptionKey);
  }

  private SecretKey getSecretKey(String encryptionKey){
    byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
  }


  public String encrypt(String data) throws Exception {
    Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
    byte[] iv = new byte[GCM_IV_LENGTH];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

    byte[] encryptedDataWithIv = new byte[iv.length + encryptedBytes.length];
    System.arraycopy(iv, 0, encryptedDataWithIv, 0, iv.length);
    System.arraycopy(encryptedBytes, 0, encryptedDataWithIv, iv.length, encryptedBytes.length);

    return Base64.getEncoder().encodeToString(encryptedDataWithIv);
  }

  public String decrypt(String encryptedData) throws Exception {
    byte[] decodedData = Base64.getDecoder().decode(encryptedData);
    byte[] iv = new byte[GCM_IV_LENGTH];
    byte[] encryptedBytes = new byte[decodedData.length - GCM_IV_LENGTH];

    System.arraycopy(decodedData, 0, iv, 0, iv.length);
    System.arraycopy(decodedData, iv.length, encryptedBytes, 0, encryptedBytes.length);

    Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }
}
