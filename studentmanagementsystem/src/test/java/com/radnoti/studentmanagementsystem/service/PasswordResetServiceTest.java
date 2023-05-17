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

    /**
     * Test case for the generatePasswordResetCode method in the PasswordResetService class.
     * It verifies that the method generates a reset code and saves the password reset details correctly.
     */
    @Test
    void generatePasswordResetCode_shouldGenerateResetCodeAndSavePasswordReset() {
        // Arrange
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


    /**
     * Test case for the generatePasswordResetCode method in the PasswordResetService class.
     * It verifies that the method throws a UserNotExistException when the user does not exist.
     */
    @Test
    void generatePasswordResetCode_shouldThrowUserNotExistException() {
        // Arrange
        String userNameNotExist = "usernotexist";
        when(userRepository.findByUsername(userNameNotExist)).thenReturn(Optional.empty());


        // Act
        // Assert
        assertThrows(UserNotExistException.class, () -> passwordResetService.generatePasswordResetCode(userNameNotExist));
}


    /**
     * Test case for the generatePasswordResetCode method in the PasswordResetService class.
     *  *
     *  * It verifies that the method throws a UserDeletedException when the user is deleted.
     */
    @Test
    void generatePasswordResetCode_shouldThrowUserDeletedException_whenUserIsDeleted() {
        // Arrange
        String userName = "testuser";
        User user = new User();
        user.setId(1);
        user.setIsDeleted(true);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        // Assert
        assertThrows(UserDeletedException.class, () -> passwordResetService.generatePasswordResetCode(userName));
    }

    /**
     * Test case for the generatePasswordResetCode method in the PasswordResetService class.
     * It verifies that the method throws a UserNotActivatedException when the user is not activated.
     */
    @Test
    void generatePasswordResetCode_shouldThrowUserNotActivatedException_whenUserIsNotActivated() {
        // Arrange
        String userName = "testuser";
        User user = new User();
        user.setId(1);
        user.setIsDeleted(false);
        user.setIsActivated(false);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        // Assert
        assertThrows(UserNotActivatedException.class, () -> passwordResetService.generatePasswordResetCode(userName));
    }


    /**
     * Test case for the resetPassword method in the PasswordResetService class.
     * It verifies that the method throws a ResetCodeNotExistException when the reset code is not found.
     */
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

        // Act
        when(userRepository.findByResetCode(userDto.getResetCode())).thenThrow(ResetCodeNotExistException.class);

        // Assert
        assertThrows(ResetCodeNotExistException.class, () -> passwordResetService.resetPassword(userDto));
        verify(userRepository).findByResetCode(userDto.getResetCode());
        verifyNoMoreInteractions(passwordResetRepository);
        verifyNoMoreInteractions(hashUtil);
    }





}
