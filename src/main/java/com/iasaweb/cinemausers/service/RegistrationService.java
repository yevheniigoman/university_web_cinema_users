package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.dto.RegisterDto;
import com.iasaweb.cinemausers.exception.UserAlreadyExistsException;
import com.iasaweb.cinemausers.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationService(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User register(RegisterDto body) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(body.username())) {
            throw new UserAlreadyExistsException(body.username());
        }
        String encodedPassword = passwordEncoder.encode(body.password());
        User user = new User(body.username(), encodedPassword, body.role());
        return userRepository.save(user);
    }
}
