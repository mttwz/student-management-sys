package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkgroupscheduleServiceTest {

    @InjectMocks
    WorkgroupScheduleService workgroupScheduleService;

    @Mock
    WorkgroupscheduleRepository workgroupscheduleRepository;
    @Mock
    WorkgroupRepository workgroupRepository;

    @Mock
    WorkgroupScheduleMapper workgroupScheduleMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    IdValidatorUtil idValidatorUtil;

    @Mock
    WorkgroupscheduleDto workgroupscheduleDto;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    JwtUtil jwtUtil;





    @Test
    public void testGetWorkgroupScheduleByUserId_SuperadminRole() {
        // Mocking dependencies and test data
        String authHeader = "Bearer [token]";
        String userIdString = "123";
        Integer userId = 123;
        Pageable pageable = Pageable.unpaged();
        Page<Workgroupschedule> workgroupSchedulePage = mock(Page.class);
        PagingDto pagingDto = new PagingDto();

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(jwtUtil.getRoleFromAuthHeader(authHeader)).thenReturn(RoleEnum.Types.SUPERADMIN);
        when(workgroupscheduleRepository.getWorkgroupScheduleByUserId(userId, pageable)).thenReturn(workgroupSchedulePage);
        when(workgroupSchedulePage.getTotalPages()).thenReturn(1);
        when(workgroupSchedulePage.stream()).thenReturn(Stream.empty());

        // Calling the method to be tested
        PagingDto result = workgroupScheduleService.getWorkgroupScheduleByUserId(authHeader, userIdString, pageable);

        // Verifying the behavior and assertions
        verify(idValidatorUtil, times(1)).idValidator(userIdString);
        verify(userRepository, times(1)).findById(userId);
        verify(jwtUtil, times(1)).getRoleFromAuthHeader(authHeader);
        verify(workgroupscheduleRepository, times(1)).getWorkgroupScheduleByUserId(userId, pageable);
        verify(workgroupScheduleMapper, never()).fromEntityToDto(any(Workgroupschedule.class));

        assertNotNull(result);
        assertEquals(1, result.getAllPages());
        assertTrue(result.getWorkgroupscheduleDtoList().isEmpty());
    }


    @Test
    public void testGetWorkgroupScheduleByUserId_InvalidUserId() {
        // Mocking dependencies and test data
        String authHeader = "authHeader";
        String userIdString = "123";
        Integer userId = 123;
        Pageable pageable = Pageable.ofSize(10);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(UserNotExistException.class, () -> {
            workgroupScheduleService.getWorkgroupScheduleByUserId(authHeader, userIdString, pageable);
        });

        // Verifying the behavior
        verify(idValidatorUtil, times(1)).idValidator(userIdString);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(jwtUtil, workgroupscheduleRepository, workgroupScheduleMapper);
    }


    @Test
    public void testGetWorkgroupScheduleByWorkgroupId() {
        // Mocking dependencies and test data
        String authHeader = "authHeader";
        WorkgroupscheduleDto workgroupScheduleDto = new WorkgroupscheduleDto();
        workgroupScheduleDto.setWorkgroupId(123);
        workgroupScheduleDto.setStart(ZonedDateTime.now());
        Pageable pageable = Pageable.ofSize(10);

        Workgroupschedule workgroupSchedule = new Workgroupschedule();
        List<Workgroupschedule> workgroupScheduleList = Arrays.asList(workgroupSchedule);
        Page<Workgroupschedule> workgroupSchedulePage = new PageImpl<>(workgroupScheduleList, pageable, workgroupScheduleList.size());

        when(workgroupRepository.findById(anyInt())).thenReturn(Optional.of(new Workgroup()));
        when(jwtUtil.getRoleFromAuthHeader(anyString())).thenReturn(RoleEnum.Types.ADMIN);
        when(workgroupscheduleRepository.getWorkgroupScheduleByWorkgroupId(anyInt(), anyString(), any(Pageable.class))).thenReturn(workgroupSchedulePage);

        // Calling the method to be tested
        PagingDto result = workgroupScheduleService.getWorkgroupScheduleByWorkgroupId(authHeader, workgroupScheduleDto, pageable);

        // Asserting the result
        assertNotNull(result);
        assertEquals(1, result.getWorkgroupscheduleDtoList().size());
        assertEquals(1, result.getAllPages());
    }



    @Test
    public void testDeleteWorkgroupSchedule() {
        // Mocking dependencies and test data
        String workgroupScheduleIdString = "1";

        Workgroupschedule workgroupSchedule = new Workgroupschedule();
        workgroupSchedule.setId(1);
        workgroupSchedule.setIsDeleted(false);

        when(idValidatorUtil.idValidator(workgroupScheduleIdString)).thenReturn(1);
        when(workgroupscheduleRepository.findById(1)).thenReturn(Optional.of(workgroupSchedule));

        // Calling the method to be tested
        workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleIdString);

        // Verifying the changes made to the workgroup schedule
        assertTrue(workgroupSchedule.getIsDeleted());
        assertNotNull(workgroupSchedule.getDeletedAt());
    }


    @Test
    public void testDeleteWorkgroupSchedule_WorkgroupScheduleNotExistException() {
        // Mocking dependencies and test data
        String workgroupScheduleIdString = "1";

        when(idValidatorUtil.idValidator(workgroupScheduleIdString)).thenReturn(1);
        when(workgroupscheduleRepository.findById(1)).thenReturn(Optional.empty());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(WorkgroupScheduleNotExistException.class, () -> {
            workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleIdString);
        });
    }

    @Test
    public void testDeleteWorkgroupSchedule_WorkgroupAlreadyDeletedException() {
        // Mocking dependencies and test data
        String workgroupScheduleIdString = "1";

        Workgroupschedule workgroupSchedule = new Workgroupschedule();
        workgroupSchedule.setId(1);
        workgroupSchedule.setIsDeleted(true);

        when(idValidatorUtil.idValidator(workgroupScheduleIdString)).thenReturn(1);
        when(workgroupscheduleRepository.findById(1)).thenReturn(Optional.of(workgroupSchedule));

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(WorkgroupAlreadyDeletedException.class, () -> {
            workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleIdString);
        });
    }

    @Test
    public void testRestoreDeletedWorkgroupSchedule() {
        // Mocking dependencies and test data
        String workgroupScheduleIdString = "1";

        Workgroupschedule workgroupSchedule = new Workgroupschedule();
        workgroupSchedule.setId(1);
        workgroupSchedule.setIsDeleted(true);
        workgroupSchedule.setDeletedAt(ZonedDateTime.now());

        when(idValidatorUtil.idValidator(workgroupScheduleIdString)).thenReturn(1);
        when(workgroupscheduleRepository.findById(1)).thenReturn(Optional.of(workgroupSchedule));

        // Calling the method to be tested
        workgroupScheduleService.restoreDeletedWorkgroupSchedule(workgroupScheduleIdString);

        // Verifying the changes made to the workgroup schedule
        assertFalse(workgroupSchedule.getIsDeleted());
        assertNull(workgroupSchedule.getDeletedAt());
    }






    @Test
    public void testGetWorkgroupScheduleByWorkgroupId_InvalidWorkgroupId() {
        // Mocking dependencies and test data
        String authHeader = "authHeader";
        WorkgroupscheduleDto workgroupScheduleDto = new WorkgroupscheduleDto();
        workgroupScheduleDto.setWorkgroupId(123);
        workgroupScheduleDto.setStart(ZonedDateTime.now());
        Pageable pageable = Pageable.ofSize(10);

        when(workgroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(WorkgroupNotExistException.class, () -> {
            workgroupScheduleService.getWorkgroupScheduleByWorkgroupId(authHeader, workgroupScheduleDto, pageable);
        });
    }


    @Test
    public void createWorkgroupScheduleTest_not_created(){
        Workgroup workgroup = new Workgroup(1);

        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setName("teszt");
        workgroupscheduleDto.setId(1);


        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName("teszt");
        workgroupschedule.setId(1);


        assertThrows(FormValueInvalidException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto));

    }

    @Test
    public void createWorkgroupScheduleTest_workgroup_does_not_exist(){
        Workgroup workgroup = new Workgroup(1);

        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setName("teszt");
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setWorkgroupId(1);
        workgroupscheduleDto.setStart(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));
        workgroupscheduleDto.setEnd(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));
        workgroupscheduleDto.setIsOnsite(true);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName("teszt");
        workgroupschedule.setId(1);
        workgroupschedule.setWorkgroupId(workgroup);
        workgroupschedule.setStart(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));
        workgroupschedule.setEnd(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));
        workgroupschedule.setIsOnsite(true);

        when(workgroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkgroupNotExistException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto));

    }

    





}













