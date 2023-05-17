package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;

import com.radnoti.studentmanagementsystem.model.dto.PagingDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserInfoDto;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import liquibase.pro.packaged.M;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.security.NoSuchAlgorithmException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;




@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public final class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserDto userDto;

    @Mock
    UserInfoDto userInfoDtoDto;

    @Mock
    UserRepository userRepository;

    @Mock
    WorkgroupRepository workgroupRepository;

    @Mock
    WorkgroupService workgroupService;

    @Mock
    RoleService roleService;

    @Mock
    UserMapper userMapper;

    @Mock
    HashUtil hashUtil;

    @Mock
    IdValidatorUtil idValidatorUtil;


    @Test
    public void  addUserTest_valid_superadmin() throws NoSuchAlgorithmException {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");


        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("password");
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        Integer actual = userService.adduser(userDto).getId();


        assertEquals(1, actual);
    }



    @Test
    public void addUserTest_valid_admin() throws NoSuchAlgorithmException {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("password");
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        Integer actual = userService.adduser(userDto).getId();


        assertEquals(1, actual);
    }

    @Test
    public void addUserTest_valid_student() throws NoSuchAlgorithmException {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("password");
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        Integer actual = userService.adduser(userDto).getId();


        assertEquals(1, actual);
    }

    @Test()
    public void addUserTest_one_field_is_empty() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");


        //act & assert
        assertThrows(FormValueInvalidException.class, () -> userService.adduser(userDto));
    }

    @Test
    public void addUserTest_one_field_is_null() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName(null);
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");


        //assert
        assertThrows(FormValueInvalidException.class, () -> userService.adduser(userDto));
    }

    @Test()
    public void addUserTest_email_already_exist() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        //act & assert
        assertThrows(UserAlreadyExistException.class, () -> userService.adduser(userDto));
    }

    @Test
    public void addUserTest_not_save(){
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("Teszt");
        userDto.setLastName("User");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("test@email.com");
        userDto.setPassword("password");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, ()->userService.adduser(userDto));
    }




    @Test
    public void testSetUserIsActivated_ValidActivationCodeAndPassword_ActivatesUser() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setActivationCode("123456");
        userDto.setPassword("password");

        User user = new User();
        user.setActivationCode("123456");
        user.setPassword(hashUtil.getSHA256Hash("password"));

        when(userRepository.findByActivationCode("123456")).thenReturn(Optional.of(user));

        // Act
        userService.setUserIsActivated(userDto);

        // Assert
        verify(userRepository).findByActivationCode("123456");
        assertTrue(user.getIsActivated());
        assertNotNull(user.getActivatedAt());
    }

    @Test
    public void testSetUserIsActivated_InvalidActivationCode_ThrowsUserNotExistException() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setActivationCode("invalid");
        userDto.setPassword("password");

        when(userRepository.findByActivationCode("invalid")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotExistException.class, () -> userService.setUserIsActivated(userDto));
        verify(userRepository).findByActivationCode("invalid");
    }

    @Test
    public void testSetUserIsActivated_IncorrectPassword_ThrowsUserNotExistException() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setActivationCode("123456");
        userDto.setPassword("incorrect");

        User user = new User();
        user.setActivationCode("123456");
        user.setPassword(hashUtil.getSHA256Hash("password"));

        when(userRepository.findByActivationCode("123456")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotExistException.class, () -> userService.setUserIsActivated(userDto));
    }

    @Test
    public void testSetUserIsActivated_DeletedUser_ThrowsUserAlreadyDeletedException() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setActivationCode("123456");
        userDto.setPassword("password");

        User user = new User();
        user.setActivationCode("123456");
        user.setPassword(hashUtil.getSHA256Hash("password"));
        user.setIsDeleted(true);

        when(userRepository.findByActivationCode("123456")).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserAlreadyDeletedException.class, () -> userService.setUserIsActivated(userDto));
        verify(userRepository).findByActivationCode("123456");

    }

    @Test
    public void testSetUserIsActivated_AlreadyActivatedUser_ThrowsUserAlreadyActivatedException() throws NoSuchAlgorithmException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setActivationCode("123456");
        userDto.setPassword("password");

        User user = new User();
        user.setActivationCode("123456");
        user.setPassword(hashUtil.getSHA256Hash("password"));
        user.setIsActivated(true);

        when(userRepository.findByActivationCode("123456")).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserAlreadyActivatedException.class, () -> userService.setUserIsActivated(userDto));
        verify(userRepository).findByActivationCode("123456");
    }



    @Test
    public void testDeleteUser_ValidUserId_DeletesUser() {
        // Arrange
        String userIdString = "1";
        Integer userId = idValidatorUtil.idValidator(userIdString);
        User user = new User();
        user.setId(userId);
        user.setIsDeleted(false);



        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(userIdString);

        // Assert
        verify(userRepository).findById(userId);
        assertTrue(user.getIsDeleted());
        assertNotNull(user.getDeletedAt());
    }

    @Test
    public void testDeleteUser_UserAlreadyDeleted_ThrowsUserAlreadyDeletedException() {
        // Arrange
        String userIdString = "1";
        Integer userId = idValidatorUtil.idValidator(userIdString);
        User user = new User();
        user.setId(userId);
        user.setIsDeleted(true);

        // Configure the findById method to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserAlreadyDeletedException.class, () -> userService.deleteUser(userIdString));
    }


    @Test
    public void testDeleteUser_UserNotExist_ThrowsUserNotExistException() {
        // Arrange
        String userIdString = "1";
        Integer userId = idValidatorUtil.idValidator(userIdString);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotExistException.class, () -> userService.deleteUser(userIdString));

    }


    @Test
    public void testRestoreDeletedUser_ValidUserId_RestoresUser() {
        // Arrange
        String userIdString = "1";
        Integer userId = idValidatorUtil.idValidator(userIdString);
        User user = new User();
        user.setId(userId);
        user.setIsDeleted(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.restoreDeletedUser(userIdString);

        // Assert
        verify(userRepository).findById(userId);
        assertFalse(user.getIsDeleted());
        assertNull(user.getDeletedAt());
    }

    @Test
    public void testRestoreDeletedUser_UserNotDeleted_ThrowsUserNotDeletedException() {
        // Arrange
        String userIdString = "1";
        Integer userId = idValidatorUtil.idValidator(userIdString);
        User user = new User();
        user.setId(userId);
        user.setIsDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserNotDeletedException.class, () -> userService.restoreDeletedUser(userIdString));
    }







}
