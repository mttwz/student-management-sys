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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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

        Integer actual = workgroupService.createWorkgroup(workgroupDto).getId();

        assertEquals(1,actual);
    }

    @Test
    public void createWorkgroupTest_not_create(){
        //arrange
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setGroupName("testName");

        Workgroup workgroup = new Workgroup();
        workgroup.setId(1);
        workgroup.setGroupName("testName");

        assertThrows(InvalidFormValueException.class,()->workgroupService.createWorkgroup(workgroupDto));
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


        Integer actual = workgroupService.addUserToWorkgroup(workgroupmembersDto).getId();

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
    public void deleteWorkgroupTest_valid(){

        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setId(1);
        workgroupDto.setIsDeleted(false);

        String workgroupId = workgroupDto.getId().toString();


        Workgroup mockWorkgroup = mock(Workgroup.class);

        mockWorkgroup.setId(1);

        when(workgroupRepository.findById(any())).thenReturn(Optional.of(mockWorkgroup));

        workgroupService.deleteWorkgroup(workgroupId);

        verify(mockWorkgroup,times(1)).setIsDeleted(true);
    }

    @Test
    public void deleteWorkgroupTest_workgroup_not_exist(){
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setId(1);
        workgroupDto.setIsDeleted(false);

        String workgroupId = workgroupDto.getId().toString();


        when(workgroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkgroupNotExistException.class, () -> workgroupService.deleteWorkgroup(workgroupId));

    }

    @Test
    public void deleteWorkgroupTest_workgroup_already_deleted(){
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setId(1);
        workgroupDto.setIsDeleted(true);

        String workgroupId = workgroupDto.getId().toString();


        Workgroup workgroup = new Workgroup();
        workgroup.setId(1);
        workgroup.setIsDeleted(true);

        when(workgroupRepository.findById(any())).thenReturn(Optional.of(workgroup));

        assertThrows(InvalidFormValueException.class, () -> workgroupService.deleteWorkgroup(workgroupId));

    }


}
