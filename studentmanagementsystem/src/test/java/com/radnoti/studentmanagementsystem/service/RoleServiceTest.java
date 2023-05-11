package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.RoleRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private RoleService roleService;

    @Test
    public void testSetUserRole_ValidUserDto_SetsUserRole() {
        // Arrange
        UserDto validUserDto = new UserDto();
        validUserDto.setId(1);
        validUserDto.setRoleName("admin");

        Mockito.when(userRepository.findById(validUserDto.getId())).thenReturn(Optional.of(new User()));
        Mockito.when(roleRepository.findByRoleName(validUserDto.getRoleName())).thenReturn(Optional.of(new Role()));

        // Act
        roleService.setUserRole(validUserDto);

        // Assert
        // ... perform assertions as necessary to verify that the user role was set successfully
        Mockito.verify(userRepository, Mockito.times(1)).setUserRole(validUserDto.getId(), validUserDto.getRoleName());
    }

    @Test
    public void testSetUserRole_MissingRoleName_ThrowsFormValueInvalidException() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        // Assert
        assertThrows(FormValueInvalidException.class, () -> {
            roleService.setUserRole(userDto);
        });
    }








}
