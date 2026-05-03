package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.dto.RegisterDto;
import com.iasaweb.cinemausers.exception.UserAlreadyExistsException;
import com.iasaweb.cinemausers.repository.UserRepository;

import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class RegistrationService {
    private final UserRepository userRepository;

    public RegistrationService(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User register(RegisterDto body) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(body.username())) {
            throw new UserAlreadyExistsException(body.username());
        }
        String encodedPassword = BCrypt.withDefaults().hashToString(12, body.password().toCharArray());
        var user = new User(body.username(), encodedPassword, body.role());
        return userRepository.save(user);
    }
}
