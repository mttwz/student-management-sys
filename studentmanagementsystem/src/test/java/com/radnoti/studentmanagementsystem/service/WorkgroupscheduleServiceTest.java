package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkgroupscheduleServiceTest {

    @InjectMocks
    WorkgroupScheduleService workgroupScheduleService;

    @Mock
    WorkgroupscheduleRepository workgroupscheduleRepository;
    @Mock
    WorkgroupRepository workgroupRepository;


    @Test
    public void createWorkgroupScheduleTest_valid() throws ParseException {
        //arrange
        String name = "Test";
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setName(name);
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setWorkgroupId(1);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName(name);
        workgroupschedule.setId(1);
        workgroupschedule.setWorkgroupId(new Workgroup(1));

        Workgroup workgroup = new Workgroup(1);

        //when(workgroupscheduleRepository.createWorkgroupSchedule(any(),any(),any(),any(),any()))
                //.thenReturn(1);
        when(workgroupRepository.findById(any(Integer.class))).thenReturn(Optional.of(workgroup));
        when(workgroupscheduleRepository.findById(any(Integer.class))).thenReturn(Optional.of(workgroupschedule));
        //act
        Integer actual = workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto).getId();

        //assert
        assertEquals(1,actual);

    }



    @Test
    public void createWorkgroupScheduleTest_not_created(){
        String name = "Test";
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        Workgroup workgroup = new Workgroup(1);

        //when(workgroupscheduleRepository.createWorkgroupSchedule(any(),any(),any(),any(),any())).thenReturn(1);
        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.empty());
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));

        Exception ex = assertThrows(ResponseStatusException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto));
        String expectedMessage = "409 CONFLICT \"Schedule not created\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createWorkgroupScheduleTest_workgroup_does_not_exist(){
        String name = "Test";
        WorkgroupscheduleDto workgroupscheduleDto = new WorkgroupscheduleDto();
        workgroupscheduleDto.setName(name);
        workgroupscheduleDto.setId(1);
        workgroupscheduleDto.setWorkgroupId(1);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName(name);
        workgroupschedule.setId(1);
        workgroupschedule.setWorkgroupId(new Workgroup(1));

        when(workgroupRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResponseStatusException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDto));
        String expectedMessage = "400 BAD_REQUEST \"Workgroup does not exist\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }

}













