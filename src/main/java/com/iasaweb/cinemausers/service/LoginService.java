package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.dto.LoginDto;
import com.iasaweb.cinemausers.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class LoginService {
    private final UserService userService;
    private final JwtCreationService jwtService;
    private final AuthenticationManager authManager;

    public LoginService(
        UserService userService,
        JwtCreationService jwtService,
        AuthenticationManager authManager
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public String login(LoginDto body) throws UserNotFoundException {
        var token = new UsernamePasswordAuthenticationToken(body.username(), body.password());
        authManager.authenticate(token);

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(body.username());
        return jwtService.token(user);
    }
}
