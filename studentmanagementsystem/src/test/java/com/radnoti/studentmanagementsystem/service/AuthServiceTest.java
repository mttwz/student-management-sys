package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {



    @Mock
    private UserRepository userRepository;

    @Mock
    private HashUtil hashUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;


    /**
     * Test case to verify that the login() method successfully authenticates a user with valid credentials.
     * The test sets up the necessary objects and mocks, including a UserDto and a User object.
     * It configures the mock behavior for the hashUtil, userRepository, userMapper, and jwtUtil.
     * The login() method is then called with the userDto.
     * Finally, it asserts that the result is not null and the generated JWT token is as expected.
     *
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     */
    @Test
    public void testLogin_success() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");
        user.setPassword("hashedpassword");
        user.setIsActivated(true);
        user.setIsDeleted(false);

        when(hashUtil.getSHA256Hash(userDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.login(userDto.getEmail(), "hashedpassword")).thenReturn(Optional.of(user));
        when(userMapper.fromEntityToLoginDto(user)).thenReturn(new UserLoginDto());
        when(jwtUtil.generateJwt(user)).thenReturn("jwt_token");

        // Act
        UserLoginDto result = authService.login(userDto);

        // Assert
        assertNotNull(result);
        assertEquals("jwt_token", result.getJwt());
    }

    /**
     * Test case to verify that the login() method throws an InvalidCredentialsException when the user provides invalid credentials.
     * The test sets up the necessary objects, including a UserDto.
     * It configures the mock behavior for the hashUtil and userRepository to return Optional.empty() when the login credentials are checked.
     * The login() method is then called with the userDto using an assertThrows statement to catch the expected InvalidCredentialsException.
     */
    @Test
    public void testLogin_invalidCredentials() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        when(hashUtil.getSHA256Hash(userDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.login(userDto.getEmail(), "hashedpassword")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(userDto));
    }

    /**
     * Test case to verify that the login() method throws a UserNotActivatedException when the user is not activated.
     * The test sets up the necessary objects, including a UserDto and a User object with isActivated set to false.
     * It configures the mock behavior for the hashUtil and userRepository to return the specified User object when the login credentials are checked.
     * The login() method is then called with the userDto using an assertThrows statement to catch the expected UserNotActivatedException.
     */
    @Test
    public void testLogin_userNotActivated() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");
        user.setPassword("hashedpassword");
        user.setIsActivated(false);
        user.setIsDeleted(false);

        when(hashUtil.getSHA256Hash(userDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.login(userDto.getEmail(), "hashedpassword")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserNotActivatedException.class, () -> authService.login(userDto));
    }

    /**
     * Test case to verify that the login() method throws a UserDeletedException when the user is marked as deleted.
     * The test sets up the necessary objects, including a UserDto and a User object with isDeleted set to true.
     * It configures the mock behavior for the hashUtil and userRepository to return the specified User object when the login credentials are checked.
     * The login() method is then called with the userDto using an assertThrows statement to catch the expected UserDeletedException.
     */
    @Test
    public void testLogin_userDeleted() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");
        user.setPassword("hashedpassword");
        user.setIsActivated(true);
        user.setIsDeleted(true);

        when(hashUtil.getSHA256Hash(userDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.login(userDto.getEmail(), "hashedpassword")).thenReturn(Optional.of(user));


        // Act & Assert
        assertThrows(UserDeletedException.class, () -> authService.login(userDto));
    }

    /**
     * Test case to verify that the validateJwt() method correctly validates a JWT token and returns true.
     * The test sets up the necessary objects, including a UserDto with a valid JWT token.
     * It configures the mock behavior for the jwtUtil to return true when the JWT token is validated.
     * The validateJwt() method is then called with the userDto.
     * Finally, it asserts that the result is not null and true.
     */
    @Test
    public void testValidateJwt_valid() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setJwt("valid_jwt");

        when(jwtUtil.validateJwt("valid_jwt")).thenReturn(true);

        // Act
        Boolean result = authService.validateJwt(userDto);

        // Assert
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test case to verify that the validateJwt() method correctly validates a JWT token and returns false.
     * The test sets up the necessary objects, including a UserDto with an invalid JWT token.
     * It configures the mock behavior for the jwtUtil to return false when the JWT token is validated.
     * The validateJwt() method is then called with the userDto.
     * Finally, it asserts that the result is not null and false.
     */
    @Test
    public void testValidateJwt_invalid() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setJwt("invalid_jwt");

        when(jwtUtil.validateJwt("invalid_jwt")).thenReturn(false);

        // Act
        Boolean result = authService.validateJwt(userDto);

        // Assert
        assertNotNull(result);

        assertFalse(result);
    }



}
