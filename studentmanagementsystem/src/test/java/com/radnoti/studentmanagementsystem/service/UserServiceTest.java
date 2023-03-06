package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public final class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;

    @Test()
    public void addUserTest_valid(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("mate");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111,11,11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        //act
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.register(any(Integer.class),any(String.class),any(String.class),any(String.class),any(Date.class),any(String.class),any(String.class))).thenReturn(1);


        //assert
        assertEquals(1,userService.adduser(userDTO));
    }

    @Test()
    public void addUserTest_one_field_is_empty(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111,11,11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        //act
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.register(any(Integer.class),any(String.class),any(String.class),any(String.class),any(Date.class),any(String.class),any(String.class))).thenReturn(1);


        //assert
        assertThrows(ResponseStatusException.class, ()-> userService.adduser(userDTO));
    }

    @Test
    public void addUserTest_one_field_is_null(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName(null);
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111,11,11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        //act
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.register(any(Integer.class),any(String.class),any(String.class),any(String.class),any(Date.class),any(String.class),any(String.class))).thenReturn(1);

        //assert
        assertThrows(NullPointerException.class, ()-> userService.adduser(userDTO));
    }

    @Test()
    public void addUserTest_email_already_exist(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
        userDTO.setFirstName("mate");
        userDTO.setLastName("");
        userDTO.setPhone("123");
        userDTO.setBirth(new Date(1111,11,11));
        userDTO.setEmail("mate");
        userDTO.setPassword("mate");

        //act
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new User()));
        when(userRepository.register(any(Integer.class),any(String.class),any(String.class),any(String.class),any(Date.class),any(String.class),any(String.class))).thenReturn(1);


        //assert
        assertThrows(ResponseStatusException.class, ()-> userService.adduser(userDTO));
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
        assertEquals(1,userService.setUserRole(userDTO));
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
        assertThrows(ResponseStatusException.class, ()-> userService.setUserRole(userDTO));
    }
}
