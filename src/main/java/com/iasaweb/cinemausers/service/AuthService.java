package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.entity.Role;
import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.dto.LoginRequestBody;
import com.iasaweb.cinemausers.dto.LoginResponseBody;
import com.iasaweb.cinemausers.dto.RegisterRequestBody;
import com.iasaweb.cinemausers.exception.UserAlreadyExistsException;
import com.iasaweb.cinemausers.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserService userService,
        JwtService jwtService,
        AuthenticationManager authManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequestBody body) throws UserAlreadyExistsException {
        String encodedPassword = passwordEncoder.encode(body.password());
        User user = new User(body.username(), encodedPassword, Role.ROLE_ADMIN);
        return userService.create(user);
    }

    public LoginResponseBody login(LoginRequestBody body) throws UserNotFoundException {
        var authToken = new UsernamePasswordAuthenticationToken(
            body.username(),
            body.password()
        );
        authManager.authenticate(authToken);

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(body.username());
        String jwtToken = jwtService.token(user);
        return new LoginResponseBody(jwtToken);
    }
}
