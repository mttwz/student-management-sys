package com.radnoti.studentmanagementsystem.service;
import com.radnoti.studentmanagementsystem.exception.passwordReset.ResetCodeNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.*;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.PasswordResetRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;

import com.radnoti.studentmanagementsystem.security.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.jupiter.api.Assertions.assertEquals;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.entity.User;

import org.mockito.Mock;


import java.util.Optional;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    private static final int ACTIVATION_CODE_LENGTH = 8;

    private static final int ACTIVATION_CODE_VALID_IN_HOURS = 2;

    @Mock
    private HashUtil hashUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetRepository passwordResetRepository;


    @InjectMocks
    private PasswordResetService passwordResetService;

    @Captor
    private ArgumentCaptor<Passwordreset> passwordResetCaptor;

    @Test
    void generatePasswordResetCode_shouldGenerateResetCodeAndSavePasswordReset() {
        String userName = "testuser";
        User user = new User();
        user.setId(1);
        user.setIsDeleted(false);
        user.setIsActivated(true);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(hashUtil.generateRandomString(anyInt())).thenReturn("randomcode");

        // Act
        passwordResetService.generatePasswordResetCode(userName);

        // Assert
        verify(userRepository).findByUsername(userName);
        verify(passwordResetRepository).save(passwordResetCaptor.capture());

        Passwordreset capturedPasswordReset = passwordResetCaptor.getValue();
        assertEquals(user, capturedPasswordReset.getUserId());
        assertEquals("randomcode", capturedPasswordReset.getResetCode());
        assertFalse(capturedPasswordReset.getIsUsed());
    }





    @Test
    void generatePasswordResetCode_shouldThrowUserNotExistException() {

        String userNameNotExist = "usernotexist";
        when(userRepository.findByUsername(userNameNotExist)).thenReturn(Optional.empty());
        assertThrows(UserNotExistException.class, () -> passwordResetService.generatePasswordResetCode(userNameNotExist));
}


    @Test
    void generatePasswordResetCode_shouldThrowUserDeletedException_whenUserIsDeleted() {
        String userName = "testuser";
        User user = new User();
        user.setId(1);
        user.setIsDeleted(true);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(UserDeletedException.class, () -> passwordResetService.generatePasswordResetCode(userName));
    }

    @Test
    void generatePasswordResetCode_shouldThrowUserNotActivatedException_whenUserIsNotActivated() {
        String userName = "testuser";
        User user = new User();
        user.setId(1);
        user.setIsDeleted(false);
        user.setIsActivated(false);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(UserNotActivatedException.class, () -> passwordResetService.generatePasswordResetCode(userName));
    }



    @Test
    void resetPassword_shouldThrowResetCodeNotExistException() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setResetCode("resetCode123");
        userDto.setPassword("newPassword");

        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        // Mock the behavior when the reset code is not found
        when(userRepository.findByResetCode(userDto.getResetCode())).thenThrow(ResetCodeNotExistException.class);

        // Act and Assert
        assertThrows(ResetCodeNotExistException.class, () -> passwordResetService.resetPassword(userDto));
        verify(userRepository).findByResetCode(userDto.getResetCode());
        verifyNoMoreInteractions(passwordResetRepository);
        verifyNoMoreInteractions(hashUtil);
    }





}