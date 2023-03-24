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
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.fromEntityToLoginDto(user)).thenReturn(new UserLoginDto());
        when(jwtUtil.generateJwt(user)).thenReturn("jwt_token");

        // Act
        UserLoginDto result = authService.login(userDto);

        // Assert
        assertNotNull(result);
        assertEquals("jwt_token", result.getJwt());
    }

    @Test
    public void testLogin_invalidCredentials() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        when(hashUtil.getSHA256Hash(userDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.login(userDto.getEmail(), "hashedpassword")).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(userDto));
    }

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
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserNotActivatedException.class, () -> authService.login(userDto));
    }

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
