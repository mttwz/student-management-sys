package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
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
public class WorkgroupServiceTest {

    @InjectMocks
    WorkgroupService workgroupService;
    @Mock
    WorkgroupRepository workgroupRepository;


    @Test
    public void createWorkgroupTest_valid(){
        //arrange
        String worgroupName = "testName";
        WorkgroupDTO workgroupDTO = new WorkgroupDTO();
        Workgroup workgroup = new Workgroup();
        workgroup.setGroupName(worgroupName);
        workgroupDTO.setGroupName(worgroupName);

        when(workgroupRepository.createWorkgroup(any(),any())).thenReturn(1);
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));
        //act
        int actual = workgroupService.createWorkgroup(workgroupDTO);
        //assert
        assertEquals(1,actual);
    }

    @Test
    public void createWorkgroupTest_not_create(){
        //arrange
        String worgroupName = "testName";
        WorkgroupDTO workgroupDTO = new WorkgroupDTO();
        Workgroup workgroup = new Workgroup();
        workgroup.setGroupName(worgroupName);
        workgroupDTO.setGroupName(worgroupName);

        when(workgroupRepository.createWorkgroup(any(),any())).thenReturn(1);
        when(workgroupRepository.findById(any())).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ResponseStatusException.class,()->workgroupService.createWorkgroup(workgroupDTO));
    }


}
