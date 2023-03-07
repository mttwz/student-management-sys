package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

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



    @Test
    public void createWorkgroupScheduleTest_valid(){
        //arrage
        String name = "Test";
        WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO();
        workgroupscheduleDTO.setName(name);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName(name);

        when(workgroupscheduleRepository.createWorkgroupSchedule(any(),any(),any(),any(),any()))
                .thenReturn(1);
        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.of(workgroupschedule));

        //act
        int actual = workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDTO);

        //assert
        assertEquals(1,actual);

    }


    @Test
    public void createWorkgroupScheduleTest_not_created(){

        String name = "Test";
        WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO();
        workgroupscheduleDTO.setName(name);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName(name);

        when(workgroupscheduleRepository.createWorkgroupSchedule(any(),any(),any(),any(),any()))
                .thenReturn(1);
        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.empty());



        assertThrows(ResponseStatusException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDTO));


    }

    @Test
    public void createWorkgroupScheduleTest_workgroup_does_not_exist(){
        String name = "Test";
        WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO();
        workgroupscheduleDTO.setName(name);

        Workgroupschedule workgroupschedule = new Workgroupschedule();
        workgroupschedule.setName(name);

        when(workgroupscheduleRepository.createWorkgroupSchedule(any(),any(),any(),any(),any()))
                .thenReturn(1);
        when(workgroupscheduleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->workgroupScheduleService.createWorkgroupSchedule(workgroupscheduleDTO));

    }

}













