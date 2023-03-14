package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
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
    JwtUtil jwtUtil;

    @Mock
    UserMapper userMapper;

    @Test
    public void loginTest_valid(){
        //arrange
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(false);
        user.setEmail("testEmail");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setId(1);
        userLoginDTO.setEmail("testEmail");
        userLoginDTO.setFirstName("testFirstName");
        userLoginDTO.setLastName("testLastName");

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtUtil.generateJwt(any())).thenReturn("veryVerySecretJwt");
        when(userMapper.fromEntityToLoginDto(any())).thenReturn(userLoginDTO);


        //act

        UserLoginDTO actual = authService.login(userDTO);

        //assert

        assertEquals(1,actual.getId());
        assertEquals("testFirstName",actual.getFirstName());
        assertEquals("testLastName",actual.getLastName());
        assertEquals("testEmail",actual.getEmail());
        assertEquals("veryVerySecretJwt",actual.getJwt());

    }

    @Test
    public void loginTest_invalid(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("mate");

        when(userRepository.login(any(),any())).thenReturn(1);

        //act & assert

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(403, actuaStatusCode);
        assertEquals("FORBIDDEN", actualStatusCodeName);
        assertEquals("Invalid username or password", actualMessage);


    }




    @Test
    public void loginTest_empty_username(){
        //arrange
        UserDTO userDTO = new UserDTO();

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(403, actuaStatusCode);
        assertEquals("FORBIDDEN", actualStatusCodeName);
        assertEquals("Invalid username or password", actualMessage);

    }

    @Test
    public void loginTest_null_userId(){
        //arrange
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(null);

        when(userRepository.login(any(),any())).thenReturn(null);


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
        user.setId(1);
        user.setIsActivated(false);
        user.setIsDeleted(false);

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(403, actuaStatusCode);
        assertEquals("FORBIDDEN", actualStatusCodeName);
        assertEquals("User not activated", actualMessage);

    }

    @Test
    public void loginTest_user_is_deleted(){
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(true);

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> authService.login(userDTO));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(403, actuaStatusCode);
        assertEquals("User is deleted", actualMessage);

    }


    @Test
    public void validateJwtTest_valid(){
        //arrange
        UserDTO userDTO = new UserDTO();
        when(jwtUtil.validateJwt(any())).thenReturn(true);
        Map expected = new HashMap<>();
        expected.put("valid",true);

        //act
        Map actual = authService.validateJwt(userDTO);
        //assert
        assertEquals(expected, actual);
    }


}
