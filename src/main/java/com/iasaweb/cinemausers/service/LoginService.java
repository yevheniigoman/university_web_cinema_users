package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.dto.LoginDto;
import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.exception.UserNotFoundException;

import com.iasaweb.cinemausers.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final JwtCreationService jwtService;

    public LoginService(
        UserRepository userRepository,
        JwtCreationService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String login(LoginDto body) throws UserNotFoundException {
        User user = userRepository.findByUsername(body.username())
                .orElseThrow(() -> new UserNotFoundException(body.username()));
        return jwtService.token(user);
    }
}
