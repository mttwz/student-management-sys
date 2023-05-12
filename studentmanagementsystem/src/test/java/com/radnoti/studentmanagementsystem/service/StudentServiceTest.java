package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserAlreadyExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testRegisterStudent() throws NoSuchAlgorithmException {
        // Mocking dependencies and test data
        UserDto userDto = new UserDto();
        userDto.setPassword("password");

        ResponseDto savedUserIdResponse = new ResponseDto(123);

        User user = new User();
        user.setId(123);

        when(userService.adduser(any(UserDto.class))).thenReturn(savedUserIdResponse);
        when(userRepository.findById(123)).thenReturn(Optional.of(user));

        // Calling the method to be tested
        ResponseDto response = studentService.registerStudent(userDto);

        // Verifying the behavior and assertions
        verify(userService, times(1)).adduser(userDto);
        verify(userRepository, times(1)).findById(123);
        verify(studentRepository, times(1)).save(any(Student.class));

        assertNotNull(response);
        assertEquals(savedUserIdResponse.getId(), response.getId());
    }



    @Test
    public void testRegisterStudent_NullPassword_ThrowsFormValueInvalidException() {
        // Mocking dependencies and test data
        UserDto userDto = new UserDto();

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(FormValueInvalidException.class, () -> {
            studentService.registerStudent(userDto);
        });


    }

}






