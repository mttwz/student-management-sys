package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
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
        UserDto userDto = new UserDto();

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(false);
        user.setEmail("testEmail");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setId(1);
        userLoginDto.setEmail("testEmail");
        userLoginDto.setFirstName("testFirstName");
        userLoginDto.setLastName("testLastName");

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtUtil.generateJwt(any())).thenReturn("veryVerySecretJwt");
        when(userMapper.fromEntityToLoginDto(any())).thenReturn(userLoginDto);


        //act

        UserLoginDto actual = authService.login(userDto);

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
        UserDto userDto = new UserDto();
        userDto.setEmail("mate");

        when(userRepository.login(any(),any())).thenReturn(1);

       assertThrows(InvalidCredentialsException.class, ()-> authService.login(userDto));



    }




    @Test
    public void loginTest_empty_username(){
        //arrange
        UserDto userDto = new UserDto();

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, ()-> authService.login(userDto));

    }

    @Test
    public void loginTest_null_userId(){
        //arrange
        UserDto userDto = new UserDto();

        User user = new User();
        user.setId(null);

        when(userRepository.login(any(),any())).thenReturn(null);


       assertThrows(InvalidCredentialsException.class, ()-> authService.login(userDto));

    }

    @Test
    public void loginTest_user_does_not_exist(){
        //arrange
        UserDto userDto = new UserDto();
        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        //act & assert
        assertThrows(InvalidCredentialsException.class,()->authService.login(userDto));

    }


    @Test
    public void loginTest_user_not_activated(){
        UserDto userDto = new UserDto();

        User user = new User();
        user.setId(1);
        user.setIsActivated(false);
        user.setIsDeleted(false);

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        assertThrows(UserNotActivatedException.class, ()-> authService.login(userDto));


    }

    @Test
    public void loginTest_user_is_deleted(){
        UserDto userDto = new UserDto();

        User user = new User();
        user.setId(1);
        user.setIsActivated(true);
        user.setIsDeleted(true);

        when(userRepository.login(any(),any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        assertThrows(UserDeletedException.class, ()-> authService.login(userDto));


    }


    @Test
    public void validateJwtTest_valid(){
        //arrange
        UserDto userDto = new UserDto();
        when(jwtUtil.validateJwt(any())).thenReturn(true);
        Map expected = new HashMap<>();
        expected.put("valid",true);

        //act
        Map actual = authService.validateJwt(userDto);
        //assert
        assertEquals(expected, actual);
    }


}
