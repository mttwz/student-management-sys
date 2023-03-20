package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMembersMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupmembers;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupMembersRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkgroupServiceTest {

    @InjectMocks
    WorkgroupService workgroupService;
    @Mock
    WorkgroupRepository workgroupRepository;
    @Mock
    WorkgroupMembersRepository workgroupMembersRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    WorkgroupMapper workgroupMapper;
    @Mock
    WorkgroupMembersMapper workgroupMembersMapper;


    @Test
    public void createWorkgroupTest_valid(){

        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setGroupName("testName");
        workgroupDto.setInstitution("testInstitution");

        Workgroup workgroup = new Workgroup();
        workgroup.setId(1);
        workgroup.setGroupName("testName");
        workgroup.setInstitution("testInstitution");


        when(workgroupRepository.save(any())).thenReturn(workgroup);
        when(workgroupMapper.fromDtoToEntity(any())).thenReturn(workgroup);

        int actual = workgroupService.createWorkgroup(workgroupDto);

        assertEquals(1,actual);
    }

    @Test
    public void createWorkgroupTest_not_create(){
        //arrange
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setGroupName("testName");
        workgroupDto.setInstitution("testInstitution");

        Workgroup workgroup = new Workgroup();
        workgroup.setId(1);
        workgroup.setGroupName("testName");
        workgroup.setInstitution("testInstitution");


        assertThrows(NullPointerException.class,()->workgroupService.createWorkgroup(workgroupDto));
    }


    @Test
    public void addUserToWorkgroupTest_valid() {

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        Workgroup workgroup = new Workgroup();
        User user = new User();

        Workgroupmembers workgroupmembers = new Workgroupmembers();
        workgroupmembers.setId(1);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));
        when(workgroupMembersMapper.fromDtoToEntity(workgroupmembersDto)).thenReturn(workgroupmembers);
        when(workgroupMembersRepository.save(any())).thenReturn(workgroupmembers);

        //act
        int actual = workgroupService.addUserToWorkgroup(workgroupmembersDto);

        //equals
        assertEquals(1, actual);

    }

    @Test
    public void addUserToWorkgroupTest_user_not_exist() {

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, () -> workgroupService.addUserToWorkgroup(workgroupmembersDto));

    }

    @Test
    public void addUserToWorkgroupTest_workgroup_not_exist() {
        //asert
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();
//
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.empty());

        //act & equals
        assertThrows(WorkgroupNotExistException.class, () -> workgroupService.addUserToWorkgroup(workgroupmembersDto));

    }

    @Test
    public void addUserToWorkgroupTest_user_not_added_to_workgroup(){
        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();

        User user = new User();
        Workgroup workgroup = new Workgroup();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));

        assertThrows(UserNotAddedToWorkgroupException.class, () -> workgroupService.addUserToWorkgroup(workgroupmembersDto));

    }


}
