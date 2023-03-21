package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDto;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import liquibase.pro.packaged.M;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.security.NoSuchAlgorithmException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public final class UserServiceTest {

    @InjectMocks
    UserService userService;
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


    @Test
    public void  addUserTest_valid_superadmin() throws NoSuchAlgorithmException {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");


        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("mate");
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
        userDto.setRoleName(RoleEnum.Types.ADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("mate");
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
        userDto.setRoleName(RoleEnum.Types.STUDENT.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userMapper.fromDtoToEntity(any())).thenReturn(user);
        when(hashUtil.getSHA256Hash(any())).thenReturn("mate");
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
        userDto.setFirstName("mate");
        userDto.setLastName("");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("password");


        //act & assert
        assertThrows(InvalidFormValueException.class, () -> userService.adduser(userDto));
    }

    @Test
    public void addUserTest_one_field_is_null() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName(null);
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");


        //assert
        assertThrows(InvalidFormValueException.class, () -> userService.adduser(userDto));
    }

    @Test()
    public void addUserTest_email_already_exist() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        //act & assert
        assertThrows(UserAlreadyExistException.class, () -> userService.adduser(userDto));
    }

    @Test
    public void addUserTest_not_save(){
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN);
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("1234");
        userDto.setBirth(ZonedDateTime.of(1111,11,11,0,0,0,0, ZoneId.systemDefault()));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, ()->userService.adduser(userDto));
    }



    @Test
    public void setUserIsActivatedTest_valid(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsActivated(false);

        User mockUser = mock(User.class);

        mockUser.setId(1);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(mockUser));
        //act
        userService.setUserIsActivated(userDto);

        //assert
        verify(mockUser,times(1)).setIsActivated(true);

    }

    @Test
    public void setUserIsActivatedTest_user_not_exist(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsActivated(true);

        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, ()-> userService.setUserIsActivated(userDto.getId().toString()));

    }

    @Test
    public void setUserIsActivatedTest_user_already_activated(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsActivated(true);

        User user = new User();
        user.setIsActivated(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyActivatedException.class, ()-> userService.setUserIsActivated(userDto.getId().toString()));
    }



    @Test
    public void deleteUserTest_valid(){
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(false);

        User mockUser = mock(User.class);

        mockUser.setId(1);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(mockUser));
        //act
        userService.deleteUser(userDto.getId().toString());

        //assert
        verify(mockUser,times(1)).setIsDeleted(true);
    }

    @Test
    public void deleteUserTest_user_not_exist(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(false);


        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, ()-> userService.deleteUser(userDto.getId().toString()));

    }

    @Test
    public void deleteUserTest_user_already_deleted(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(true);

        User user = new User();
        user.setIsDeleted(true);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyDeletedException.class, ()-> userService.deleteUser(userDto.getId().toString()));
    }


    @Test
    public void getAllUserTest_valid(){

    }


    @Test
    public void getUserInfoTest_valid(){

    }

    @Test
    public void editUserInfoTest_valid(){

    }

    @Test
    public void searchSuperadminTest_valid(){

    }

    @Test
    public void getUserFromWorkgroupTest_valid(){

    }



}
