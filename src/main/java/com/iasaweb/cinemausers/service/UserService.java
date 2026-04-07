package com.iasaweb.cinemausers.service;

import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.exception.UserAlreadyExistsException;
import com.iasaweb.cinemausers.repository.UserRepository;
import com.iasaweb.cinemausers.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String name) throws UserNotFoundException {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UserNotFoundException(name));
    }

    public User create(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        return userRepository.save(user);
    }

    public UserDetailsService userDetailsService() {
        return this::findByUsername;
    }
}
