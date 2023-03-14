package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    UserRepository userRepository;


    @Test
    public void registerStudentTest_valid(){
        //arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("mate");
        userDto.setLastName("mate");
        userDto.setPhone("1234");
        userDto.setBirth(new Date(1111,11,11));
        userDto.setEmail("mate");
        userDto.setPassword("mate");

        Student student = new Student(1);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());
        when(studentRepository.registerStudent(any(String.class),any(String.class),any(String.class),any(Date.class),any(String.class),any(String.class)))
                .thenReturn(1);
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(student));
        //act
        int actual = studentService.registerStudent(userDto);

        //assert
        assertEquals(1,actual);
    }

    @Test
    public void registerStudentTest_already_exist(){
        //arrange
        UserDto userDto = new UserDto();
        
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(new User()));

        Exception ex = assertThrows(ResponseStatusException.class,()->studentService.registerStudent(userDto));
        String expectedMessage = "409 CONFLICT \"User already exist\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void registerStudentTest_not_save(){

        UserDto userDto = new UserDto();

        when(studentRepository.registerStudent(any(),any(),any(),any(),any(),any()))
                .thenReturn(1);
        when(studentRepository.findById(any()))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(ResponseStatusException.class,()->studentService.registerStudent(userDto));
        String expectedMessage = "409 CONFLICT \"User not saved\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


}
