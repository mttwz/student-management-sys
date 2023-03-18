package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDto;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


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

    @Test()
    public void addUserTest_valid_superadmin() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);


        //act
        int actual = userService.adduser(userDto);

        //assert
        assertEquals(1, actual);
    }

    @Test
    public void addUserTest_valid_admin() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.ADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);


        //act
        int actual = userService.adduser(userDto);

        //assert
        assertEquals(1, actual);
    }

    @Test
    public void addUserTest_valid_student() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.STUDENT.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("123");
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);



        //act
        int actual = userService.adduser(userDto);

        //assert
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
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //act & assert
        assertThrows(EmptyFormValueException.class, () -> userService.adduser(userDto));
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
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        //act
//        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //assert
        assertThrows(NullFormValueException.class, () -> userService.adduser(userDto));
    }

    @Test()
    public void addUserTest_email_already_exist() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDto.setFirstName("mate");
        userDto.setLastName("");
        userDto.setPhone("123");
        userDto.setBirth(new Date(1111, 11, 11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new User()));

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
        userDto.setBirth(new Date(1111,11,11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");


        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        //assert & act
//        int actual = userService.adduser(userDto);
//        assertEquals(1,actual);
        assertThrows(UserNotSavedException.class, ()->userService.adduser(userDto));
    }


    @Test
    public void getWorkgroupScheduleByUserIdTest_valid(){


    }


    @Test
    public void addUserToWorkgroupTest_valid() {
        //asert
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();
        Workgroup workgroup = new Workgroup();

        Collection<Workgroupmembers> workgroupmembersCollection = new ArrayList<>();
        workgroupmembersCollection.add(new Workgroupmembers(1));
        workgroupmembersCollection.add(new Workgroupmembers(2));
        user.setWorkgroupmembersCollection(workgroupmembersCollection);


        when(userRepository.addUserToWorkgroup(any(), any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));
        //act
        int actual = userService.addUserToWorkgroup(workgroupmembersDto);

        //equals
        assertEquals(1, actual);

    }

    @Test
    public void addUserToWorkgroupTest_user_not_exist() {
        //asert
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();
        Workgroup workgroup = new Workgroup();

        Collection<Workgroupmembers> workgroupmembersCollection = new ArrayList<>();
        workgroupmembersCollection.add(new Workgroupmembers(1));
        workgroupmembersCollection.add(new Workgroupmembers(2));
        user.setWorkgroupmembersCollection(workgroupmembersCollection);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //act & equals
        assertThrows(UserNotExistException.class, () -> userService.addUserToWorkgroup(workgroupmembersDto));

    }

    @Test
    public void addUserToWorkgroupTest_workgroup_not_exist() {
        //asert
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.empty());

        //act & equals
        assertThrows(WorkgroupNotExistException.class, () -> userService.addUserToWorkgroup(workgroupmembersDto));

    }

    @Test
    public void addUserToWorkgroupTest_user_not_added_to_workgroup(){
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();
        Workgroup workgroup = new Workgroup();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));
        when(userRepository.addUserToWorkgroup(any(), any())).thenReturn(null);
        //act & equals
        assertThrows(UserNotAddedToWorkgroupException.class, () -> userService.addUserToWorkgroup(workgroupmembersDto));

    }

    @Test
    public void setUserIsActivatedTest_valid(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsActivated(false);

        User user = new User();
        user.setIsActivated(false);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertEquals(1,userService.setUserIsActivated(userDto));
    }

    @Test
    public void setUserIsActivatedTest_user_not_exist(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsActivated(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, ()-> userService.setUserIsActivated(userDto));

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

        assertThrows(UserAlreadyActivatedException.class, ()-> userService.setUserIsActivated(userDto));
    }



    @Test
    public void deleteUserTest_valid(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(false);

        User user = new User();
        user.setIsDeleted(false);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertEquals(1,userService.deleteUser(userDto));
    }

    @Test
    public void deleteUserTest_user_not_exist(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(true);

        User user = new User();
        user.setIsDeleted(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, ()-> userService.deleteUser(userDto));

    }

    @Test
    public void deleteUserTest_user_already_deleted(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setIsDeleted(true);

        User user = new User();
        user.setIsDeleted(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyDeletedException.class, ()-> userService.deleteUser(userDto));
    }


    @Test
    //@Sql(scripts={"classpath:sqls/AuthLogin.sql"})
    public void setUserRoleTest_valid() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());

        Role role = new Role(1);
        role.setRoleType(RoleEnum.Types.SUPERADMIN.toLowerCase());

        User user = new User(1);
        user.setRoleId(role);

        //act
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        //assert
        assertEquals(1, userService.setUserRole(userDto));
    }

    @Test()
    public void setUserRoleTest_empty() {
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        //act
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        //assert
        assertThrows(UserNotExistException.class, () -> userService.setUserRole(userDto));
    }




}
