package com.iasaweb.cinemausers.controller;

import com.iasaweb.cinemausers.dto.RegisterDto;
import com.iasaweb.cinemausers.dto.LoginDto;
import com.iasaweb.cinemausers.service.RegistrationService;
import com.iasaweb.cinemausers.service.LoginService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
public class AuthController {
    private final RegistrationService registerService;
    private final LoginService loginService;

    public AuthController(
        RegistrationService registerService,
        LoginService loginService
    ) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto requestBody) {
        registerService.register(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto requestBody) {
        String jwt = loginService.login(requestBody);
        return ResponseEntity.ok(Map.of("jwt", jwt));
    }
}
