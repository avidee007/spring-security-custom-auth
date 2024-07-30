package com.example.customauth.controller;


import com.example.customauth.service.AesTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  private final AesTokenService service;

  public HelloController(AesTokenService service) {
    this.service = service;
  }

  @GetMapping("/token/{number}")
  public ResponseEntity<String> getToken(@PathVariable("number") String mobile) throws Exception {
    return ResponseEntity.ok(service.generateToken(mobile));


  }

  @GetMapping("/hello")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello World");
  }
}
