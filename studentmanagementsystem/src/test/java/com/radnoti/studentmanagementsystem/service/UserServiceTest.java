package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
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

    @Test()
    public void addUserTest_valid_superadmin() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("mate");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        //act
        int actual = userService.adduser(userDTO);

        //assert
        assertEquals(1, actual);
    }

    @Test
    public void addUserTest_valid_admin() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.ADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("mate");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        //act
        int actual = userService.adduser(userDTO);

        //assert
        assertEquals(1, actual);
    }

    @Test
    public void addUserTest_valid_student() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.STUDENT.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("mate");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        User user = new User();
        user.setId(1);
        user.setEmail("mate");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        //act
        int actual = userService.adduser(userDTO);

        //assert
        assertEquals(1, actual);
    }

    @Test()
    public void addUserTest_one_field_is_empty() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ResponseStatusException.class, () -> userService.adduser(userDTO));
    }

    @Test
    public void addUserTest_one_field_is_null() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName(null);
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        //act
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //assert
        assertThrows(ResponseStatusException.class, () -> userService.adduser(userDTO));
    }

    @Test()
    public void addUserTest_email_already_exist() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111, 11, 11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new User()));

        //act & assert
        assertThrows(ResponseStatusException.class, () -> userService.adduser(userDTO));
    }

    @Test
    public void addUserTest_not_save(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN);
        userDTO.setFirstName("mate");
        userDTO.setLastName("mate");
        userDTO.setPhone("1234");
        userDTO.setBirth(new Date(1111,11,11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        User user = new User();


        when(userRepository.register(any(Integer.class), any(String.class), any(String.class), any(String.class), any(Date.class), any(String.class), any(String.class)))
                .thenReturn(1);
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));

        //assert & act
//        int actual = userService.adduser(userDTO);
//        assertEquals(1,actual);
        assertThrows(ResponseStatusException.class, ()->userService.adduser(userDTO));
    }


    @Test
    public void getWorkgroupScheduleByUserIdTest_valid(){
        

    }


    @Test
    public void addUserToWorkgroupTest_valid() {
        //asert
        WorkgroupmembersDTO workgroupmembersDTO = new WorkgroupmembersDTO();

        User user = new User();

        Collection<Workgroupmembers> workgroupmembersCollection = new ArrayList<>();
        workgroupmembersCollection.add(new Workgroupmembers(1));
        workgroupmembersCollection.add(new Workgroupmembers(2));
        user.setWorkgroupmembersCollection(workgroupmembersCollection);


        when(userRepository.addUserToWorkgroup(any(), any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        //act
        int actual = userService.addUserToWorkgroup(workgroupmembersDTO);

        //equals
        assertEquals(1, actual);

    }

    @Test
    public void addUserToWorkgroupTest_user_not_exist() {
        //asert
        WorkgroupmembersDTO workgroupmembersDTO = new WorkgroupmembersDTO();

        User user = new User();

        Collection<Workgroupmembers> workgroupmembersCollection = new ArrayList<>();
        workgroupmembersCollection.add(new Workgroupmembers(1));
        workgroupmembersCollection.add(new Workgroupmembers(2));
        user.setWorkgroupmembersCollection(workgroupmembersCollection);


        when(userRepository.addUserToWorkgroup(any(), any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());


        //act & equals
        assertThrows(ResponseStatusException.class, () -> userService.addUserToWorkgroup(workgroupmembersDTO));

    }

    @Test
    public void addUserToWorkgroupTest_workgroup_not_exist() {
        //asert
        WorkgroupmembersDTO workgroupmembersDTO = new WorkgroupmembersDTO();

        User user = new User();

        when(userRepository.addUserToWorkgroup(any(), any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        //act & equals
        assertThrows(ResponseStatusException.class, () -> userService.addUserToWorkgroup(workgroupmembersDTO));

    }

    @Test
    public void setUserIsActivatedTest_valid(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setIsActivated(true);

        User user = new User();
        user.setIsActivated(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertEquals(1,userService.setUserIsActivated(userDTO));
    }

    @Test
    public void setUserIsActivatedTest_user_not_exist(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setIsActivated(true);

        User user = new User();
        user.setIsActivated(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, ()-> userService.setUserIsActivated(userDTO));

    }



    @Test
    public void deleteUserTest_valid(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setIsDeleted(true);

        User user = new User();
        user.setIsDeleted(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(user));

        assertEquals(1,userService.deleteUser(userDTO));
    }

    @Test
    public void deleteUserTest_user_not_exist(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setIsDeleted(true);

        User user = new User();
        user.setIsDeleted(true);

        when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, ()-> userService.deleteUser(userDTO));

    }


    @Test
    //@Sql(scripts={"classpath:sqls/asd.sql"})
    public void setUserRoleTest_valid() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());

        Role role = new Role(1);
        role.setRoleType(RoleEnum.Types.SUPERADMIN.toLowerCase());

        User user = new User(1);
        user.setRoleId(role);

        //act
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        //assert
        assertEquals(1, userService.setUserRole(userDTO));
    }

    @Test()
    public void setUserRoleTest_empty() {
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        //act
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        //assert
        assertThrows(ResponseStatusException.class, () -> userService.setUserRole(userDTO));
    }




}
