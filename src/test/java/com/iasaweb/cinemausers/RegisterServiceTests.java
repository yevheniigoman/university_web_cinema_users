package com.iasaweb.cinemausers;

import com.iasaweb.cinemausers.entity.Role;
import com.iasaweb.cinemausers.entity.User;
import com.iasaweb.cinemausers.dto.RegisterDto;
import com.iasaweb.cinemausers.repository.UserRepository;
import com.iasaweb.cinemausers.service.RegistrationService;
import com.iasaweb.cinemausers.exception.UserAlreadyExistsException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class RegisterServiceTests {
    private static final String EXISTING_USERNAME = "Existing User";
    private RegistrationService registrationService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(EXISTING_USERNAME)).thenReturn(true);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> (User) invocation.getArgument(0));

        passwordEncoder = new BCryptPasswordEncoder();
        registrationService = new RegistrationService(passwordEncoder, userRepository);
    }

    @Test
    void testNewUserRegistrationSuccess() {
        var registerDto = new RegisterDto("New User", "password", Role.ROLE_ADMIN);
        User registeredUser = registrationService.register(registerDto);

        assertEquals(registerDto.username(), registeredUser.getUsername());
        assertTrue(passwordEncoder.matches(registerDto.password(), registeredUser.getPassword()));
        assertEquals(registerDto.role(), registeredUser.getRole());
    }

    @Test
    void testExistingUserRegistrationFailed() {
        var registerDto = new RegisterDto(EXISTING_USERNAME, "password", Role.ROLE_ADMIN);
        assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(registerDto));
    }
}
