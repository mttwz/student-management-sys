package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.AttendanceMapper;
import com.radnoti.studentmanagementsystem.model.dto.AttendanceDto;

import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.DateUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;


import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private AttendanceMapper attendanceMapper;

    @Mock
    private IdValidatorUtil idValidatorUtil;


    /**
     * Test case to verify that the createAttendance() method successfully creates an attendance record when all the required conditions are met.
     * The test sets up the necessary objects and mocks, including an AttendanceDto, User, Student, and Attendance objects.
     * It configures the mock behavior for the userRepository, studentRepository, and attendanceMapper.
     * The createAttendance() method is then called with the attendanceDto.
     * Finally, it verifies that the userRepository and studentRepository methods were called with the correct arguments.
     */
    @Test
    public void test_createAttendance_successful() {

        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setUserId(1);

        User user = new User();
        user.setId(1);
        user.setIsDeleted(false);
        user.setIsActivated(true);

        Student student = new Student();
        student.setId(1);
        student.setUserId(user);

        Attendance attendance = new Attendance();
        attendance.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(Optional.of(student));
        when(attendanceMapper.fromDtoToEntity(attendanceDto)).thenReturn(attendance);

        attendanceService.createAttendance(attendanceDto);

        // Verify that the userRepository, studentRepository, and attendanceRepository methods were called with the correct arguments
        Mockito.verify(userRepository).findById(1);
        Mockito.verify(studentRepository).findByUserId(1);

    }

    /**
     * Test case to verify that the createAttendance() method throws a UserNotExistException when the user does not exist.
     * The test sets up the necessary objects, including an AttendanceDto.
     * It configures the mock behavior for the userRepository to return Optional.empty() when findById() is called with the specified user ID.
     * The createAttendance() method is then called with the attendanceDto using an assertThrows statement to catch the expected UserNotExistException.
     * Finally, it verifies that the userRepository method was called with the correct argument.
     */
    @Test
    public void testCreateAttendance_userNotExistException() {
        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, () -> {
            attendanceService.createAttendance(attendanceDto);
        });

        // Verify that the userRepository method was called with the correct argument
        Mockito.verify(userRepository).findById(1);
    }

    /**
     * Test case to verify that the createAttendance() method throws a UserDeletedException when the user is marked as deleted.
     * The test sets up the necessary objects, including an AttendanceDto and a User object with isDeleted set to true.
     * It configures the mock behavior for the userRepository to return the specified User object when findById() is called with the user ID.
     * The createAttendance() method is then called with the attendanceDto using an assertThrows statement to catch the expected UserDeletedException.
     * Finally, it verifies that the userRepository method was called with the correct argument.
     */
    @Test
    public void testCreateAttendance_userDeletedException() {
        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setUserId(1);

        User user = new User();
        user.setId(1);
        user.setIsDeleted(true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(UserDeletedException.class, () -> {
            attendanceService.createAttendance(attendanceDto);
        });

        // Verify that the userRepository method was called with the correct argument
        Mockito.verify(userRepository).findById(1);
    }


    /**
     * Test case to verify that the createAttendance() method throws a UserNotActivatedException when the user is not activated.
     * The test sets up the necessary objects, including an AttendanceDto and a User object with isActivated set to false.
     * It configures the mock behavior for the userRepository to return the specified User object when findById() is called with the user ID.
     * The createAttendance() method is then called with the attendanceDto using an assertThrows statement to catch the expected UserNotActivatedException.
     * Finally, it verifies that the userRepository method was called with the correct argument.
     */
    @Test
    public void testCreateAttendance_userNotActivatedException() {
        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setUserId(1);

        User user = new User();
        user.setId(1);
        user.setIsActivated(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(UserNotActivatedException.class, () -> {
            attendanceService.createAttendance(attendanceDto);
        });

        // Verify that the userRepository method was called with the correct argument
        verify(userRepository).findById(1);
    }





}
