package com.iasaweb.cinemausers.controller;

import com.iasaweb.cinemausers.dto.RegisterRequestBody;
import com.iasaweb.cinemausers.service.AuthService;
import com.iasaweb.cinemausers.dto.LoginRequestBody;
import com.iasaweb.cinemausers.dto.LoginResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestBody requestBody) {
        authService.register(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody LoginRequestBody requestBody) {
        LoginResponseBody responseBody = authService.login(requestBody);
        return ResponseEntity.ok(responseBody);
    }
}
