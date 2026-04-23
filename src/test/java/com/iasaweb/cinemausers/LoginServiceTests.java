package com.iasaweb.cinemausers;

import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.entity.Role;
import com.iasaweb.cinemausers.dto.LoginDto;
import com.iasaweb.cinemausers.exception.UserNotFoundException;
import com.iasaweb.cinemausers.repository.UserRepository;
import com.iasaweb.cinemausers.service.JwtCreationService;
import com.iasaweb.cinemausers.service.LoginService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@SpringBootTest
public class LoginServiceTests {
    private static final User EXISTING_USER = new User("Existing User", "password", Role.ROLE_ADMIN);
    private static final String NONEXISTING_USERNAME = "Unknown User";
    private LoginService loginService;

    @BeforeEach
    void beforeEach() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(EXISTING_USER.getUsername()))
                .thenReturn(Optional.of(EXISTING_USER));
        when(userRepository.findByUsername(NONEXISTING_USERNAME))
                .thenReturn(Optional.empty());

        JwtCreationService jwtService = mock(JwtCreationService.class);
        when(jwtService.token(any(User.class))).thenReturn("token");

        loginService = new LoginService(userRepository, jwtService);
    }

    @Test
    void testValidCredentialsLoginSuccess() {
        var loginDto = new LoginDto(EXISTING_USER.getUsername(), EXISTING_USER.getPassword());
        assertDoesNotThrow(() -> loginService.login(loginDto));
    }

    @Test
    void testBadCredentialsLoginFailed() {
        var loginDto = new LoginDto(NONEXISTING_USERNAME, "password");
        assertThrows(UserNotFoundException.class, () -> loginService.login(loginDto));
    }
}
