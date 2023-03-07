package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {



    @InjectMocks
    AuthService authService;
    @Mock
    UserRepository userRepository;

    @Mock
    JwtConfig jwtConfig;

    @Test
    public void loginTest_valid(){
        //arrange
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(false);
        user.setEmail("mate");
        user.setFirstName("mate");
        user.setLastName("mate");

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtConfig.generateJwt(any())).thenReturn("veryVerySecretJwt");
        UserLoginDTO userLoginDTO = new UserLoginDTO(1,"mate","mate","mate","veryVerySecretJwt");

        //act

        UserLoginDTO actual = authService.login(userDTO);

        //assert

        assertEquals(userLoginDTO.getId(),actual.getId());
        assertEquals(userLoginDTO.getFirstName(),actual.getFirstName());
        assertEquals(userLoginDTO.getLastName(),actual.getLastName());
        assertEquals(userLoginDTO.getEmail(),actual.getEmail());
        assertEquals(userLoginDTO.getJwt(),actual.getJwt());

    }

    @Test
    public void loginTest_invalid(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("mate");

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(false);
        user.setEmail("mateee");
        user.setFirstName("mate");
        user.setLastName("mate");

        when(userRepository.login(any(),any())).thenReturn(1);

        Exception ex = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        String expectedMessage = "403 FORBIDDEN \"Invalid username or password\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }




    @Test
    public void loginTest_empty_username(){
        //arrange
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setEmail("");


        when(userRepository.login(any(),any())).thenReturn(1);

        Exception ex = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        String expectedMessage = "403 FORBIDDEN \"Invalid username or password\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void loginTest_null_userId(){
        //arrange
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(null);

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        String expectedMessage = "403 FORBIDDEN \"Invalid username or password\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void loginTest_user_does_not_exist(){
        //arrange
        UserDTO userDTO = new UserDTO();
        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        //act & assert
        assertThrows(ResponseStatusException.class,()->authService.login(userDTO));

    }


    @Test
    public void loginTest_user_not_activated(){
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setIsActivated(false);
        user.setId(1);

        when(userRepository.login(any(),any())).thenReturn(1);

        assertThrows(ResponseStatusException.class, ()->authService.login(userDTO));

    }

    @Test
    public void loginTest_user_is_deleted(){
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setIsDeleted(true);
        user.setId(1);

        when(userRepository.login(any(),any())).thenReturn(1);

        assertThrows(ResponseStatusException.class, ()->authService.login(userDTO));

    }

}
