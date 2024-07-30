package com.example.customauth.config;

import com.example.customauth.service.AesTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomAuthFilter extends OncePerRequestFilter {

  public static final String X_AUTH_TOKEN = "x-auth-token";
  private final AesTokenService tokenService;

  public CustomAuthFilter(AesTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    if (!Collections.list(request.getHeaderNames()).contains(X_AUTH_TOKEN)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (validateAuthToken(request, response)) {
      return;
    }

    var auth = new CustomAuthToken(getAuthorities());
    var newContext = SecurityContextHolder.createEmptyContext();
    newContext.setAuthentication(auth);
    SecurityContextHolder.setContext(newContext);

    filterChain.doFilter(request, response);

  }

  private boolean validateAuthToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      if (!tokenService.validateToken(request.getHeader(X_AUTH_TOKEN))) {
        return buildUnAuthorisedResponse(response);
      }
    } catch (Exception e) {
      return buildUnAuthorisedResponse(response);
    }
    return false;
  }

  private boolean buildUnAuthorisedResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().write("Invalid token");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    return true;
  }

  private List<SimpleGrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ANONYMOUS_USER"));
  }
}
