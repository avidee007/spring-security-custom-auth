package com.example.customauth.config;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthToken extends AbstractAuthenticationToken {


  public CustomAuthToken(Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return "Anonymous user";
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }
}
