package com.example.customauth.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Token implements Serializable {
  private final String mobileNumber;
  private Instant creationTime;
  private UUID uuid;

  public Token(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    this.creationTime = Instant.now();
    this.uuid = UUID.randomUUID();
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public Instant getCreationTime() {
    return creationTime;
  }

  public UUID getUuid() {
    return uuid;
  }

  public static String serializeToken(Token token) {
    return token.getMobileNumber() + "," + token.getCreationTime().toString() + "," +
        token.getUuid().toString();
  }

  public static Token deserialize(String tokenString) {
    String[] parts = tokenString.split(",");
    if (parts.length != 3) {
      return null;
    }
    Token token = new Token(parts[0]);
    token.creationTime = Instant.parse(parts[1]);
    token.uuid = UUID.fromString(parts[2]);
    return token;
  }
}

