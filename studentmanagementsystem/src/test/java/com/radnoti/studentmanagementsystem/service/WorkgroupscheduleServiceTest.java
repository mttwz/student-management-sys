package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    JwtUtil jwtUtil;




    @Test
    public void createWorkgroupScheduleTest_valid() throws ParseException {
        //arrange
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


        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));
        when(workgroupScheduleMapper.fromDtoToEntity(workgroupscheduleDto)).thenReturn(workgroupschedule);
        when(workgroupscheduleRepository.save(any())).thenReturn(workgroupschedule);
        //act
        int actual = workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto).getId();

        //assert
        assertEquals(1,actual);

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


        assertThrows(InvalidFormValueException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto));

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


    @Test
    public void getWorkgroupScheduleByUserIdTest_valid(){
        // TODO: Atirni
//        // Arrange
//        String authHeader = "test-auth-header";
//        UserDto userDto = new UserDto();
//        userDto.setId(1);
//
//        String userId = userDto.getId().toString();
//
//        User user = new User();
//        user.setId(1);
//
//        List<Integer> workgroupScheduleList = Arrays.asList(1, 2, 3);
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//
//        when(jwtUtil.getRoleFromJwt(any())).thenReturn(RoleEnum.Types.SUPERADMIN);
//        when(workgroupscheduleRepository.getWorkgroupScheduleByUserId(any())).thenReturn(workgroupScheduleList);
//
//        Workgroupschedule workgroupschedule1 = new Workgroupschedule();
//        WorkgroupscheduleDto workgroupscheduleDto1 = new WorkgroupscheduleDto();
//        when(workgroupscheduleRepository.findAllById(any())).thenReturn(List.of(workgroupschedule1));
//        when(workgroupScheduleMapper.fromEntityToDto(any())).thenReturn(workgroupscheduleDto1);
//
//        // Act
//        List<WorkgroupscheduleDto> result = workgroupScheduleService.getWorkgroupScheduleByUserId(authHeader, userId);
//
//        // Assert
//        assertEquals(1, result.size());
    }

    @Test
    public void getWorkgroupScheduleByUserIdTest_user_not_exist(){
//        String authHeader = "test-auth-header";
//
//        UserDto userDto = new UserDto();
//        userDto.setId(1);
//
//        String userId = userDto.getId().toString();
//
//        when(userRepository.findById(any())).thenReturn(Optional.empty());
//
//        assertThrows(UserNotExistException.class,()->workgroupScheduleService.getWorkgroupScheduleByUserId(authHeader, userId));

    }

    @Test
    public void deleteWorkgroupScheduleTest_valid(){

        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setIsDeleted(false);

        String workgroupScheduleId = workgroupscheduleDto.getId().toString();

        Workgroupschedule mockWorkgroupSchedule = mock(Workgroupschedule.class);
        mockWorkgroupSchedule.setId(1);

        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.of(mockWorkgroupSchedule));

        workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleId);

        verify(mockWorkgroupSchedule, times(1)).setIsDeleted(true);
    }

    @Test
    public void deleteWorkgroupScheduleTest_workgroupSchedule_not_exist(){
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setIsDeleted(false);

        String workgroupScheduleId = workgroupscheduleDto.getId().toString();


        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkgroupScheduleNotExistException.class,()->workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleId));

    }

    @Test
    public void deleteWorkgroupScheduleTest_workgroupSchedule_already_deleted(){
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setIsDeleted(true);

        String workgroupScheduleId = workgroupscheduleDto.getId().toString();


        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setId(1);
        workgroupschedule.setIsDeleted(true);

        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.of(workgroupschedule));

        assertThrows(WorkgroupAlreadyDeletedException.class,()->workgroupScheduleService.deleteWorkgroupSchedule(workgroupScheduleId));

    }

    @Test
    public void restoreDeletedWorkgroupScheduleTest_valid(){
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setIsDeleted(true);
        workgroupscheduleDto.setDeletedAt(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));

        Workgroupschedule mockWorkgroupschedule = mock(Workgroupschedule.class);
        mockWorkgroupschedule.setId(1);
        mockWorkgroupschedule.setIsDeleted(true);
        mockWorkgroupschedule.setDeletedAt(ZonedDateTime.parse("2023-02-03T10:15:30+01:00"));


        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.of(mockWorkgroupschedule));

        workgroupScheduleService.restoreDeletedWorkgroupSchedule(workgroupscheduleDto);

        verify(mockWorkgroupschedule,times(1)).setIsDeleted(false);
        verify(mockWorkgroupschedule,times(1)).setDeletedAt(null);
    }

    @Test
    public void restoreDeletedWorkgroupScheduleTest_workgroupSchedule_not_exist(){
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setIsDeleted(false);


        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.empty());


        assertThrows(WorkgroupScheduleNotExistException.class,()->workgroupScheduleService.restoreDeletedWorkgroupSchedule(workgroupscheduleDto));

    }

}













