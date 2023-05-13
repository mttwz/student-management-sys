package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserAlreadyAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotDeletedException;
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
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
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

    @Mock
    IdValidatorUtil idValidatorUtil;



    @Test
    void testCreateWorkgroup_ValidInput_ReturnsResponseDto() {
        // Arrange
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setInstitution("Some Institution");
        workgroupDto.setGroupName("Some Group");

        Workgroup workgroup = new Workgroup();
        workgroup.setId(1);

        when(workgroupMapper.fromDtoToEntity(workgroupDto)).thenReturn(workgroup);
        when(workgroupRepository.save(workgroup)).thenReturn(workgroup);

        // Act
        ResponseDto responseDto = workgroupService.createWorkgroup(workgroupDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(1, responseDto.getId());
        verify(workgroupRepository, times(1)).save(workgroup);
    }

    @Test
    void testCreateWorkgroup_MissingFormValues_ThrowsFormValueInvalidException() {
        // Arrange
        WorkgroupDto workgroupDto = new WorkgroupDto();
        workgroupDto.setInstitution(null);
        workgroupDto.setGroupName("Some Group");

        // Act & Assert
        assertThrows(FormValueInvalidException.class, () -> workgroupService.createWorkgroup(workgroupDto));
        verifyNoInteractions(workgroupRepository);
    }

    @Test
    void testDeleteWorkgroup_ValidWorkgroupId_DeletesWorkgroup() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        Workgroup workgroup = new Workgroup();
        workgroup.setId(workgroupId);
        workgroup.setIsDeleted(false);

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));

        // Act
        workgroupService.deleteWorkgroup(workgroupIdString);

        // Assert
        assertTrue(workgroup.getIsDeleted());
        assertNotNull(workgroup.getDeletedAt());
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }

    @Test
    void testDeleteWorkgroup_NonexistentWorkgroupId_ThrowsWorkgroupNotExistException() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkgroupNotExistException.class, () -> workgroupService.deleteWorkgroup(workgroupIdString));
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }

    @Test
    void testDeleteWorkgroup_DeletedWorkgroup_ThrowsWorkgroupAlreadyDeletedException() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        Workgroup workgroup = new Workgroup();
        workgroup.setId(workgroupId);
        workgroup.setIsDeleted(true);

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));

        // Act & Assert
        assertThrows(WorkgroupAlreadyDeletedException.class, () -> workgroupService.deleteWorkgroup(workgroupIdString));
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }

    @Test
    void testRestoreDeletedWorkgroup_ValidWorkgroupId_RestoresWorkgroup() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        Workgroup workgroup = new Workgroup();
        workgroup.setId(workgroupId);
        workgroup.setIsDeleted(true);

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));

        // Act
        workgroupService.restoreDeletedWorkgroup(workgroupIdString);

        // Assert
        assertFalse(workgroup.getIsDeleted());
        assertNull(workgroup.getDeletedAt());
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }

    @Test
    void testRestoreDeletedWorkgroup_NonexistentWorkgroupId_ThrowsWorkgroupNotExistException() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkgroupNotExistException.class, () -> workgroupService.restoreDeletedWorkgroup(workgroupIdString));
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }

    @Test
    void testRestoreDeletedWorkgroup_NotDeletedWorkgroup_ThrowsWorkgroupNotDeletedException() {
        // Arrange
        Integer workgroupId = 1;
        String workgroupIdString = "1";

        Workgroup workgroup = new Workgroup();
        workgroup.setId(workgroupId);
        workgroup.setIsDeleted(false);

        when(idValidatorUtil.idValidator(workgroupIdString)).thenReturn(workgroupId);
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));

        // Act & Assert
        assertThrows(WorkgroupNotDeletedException.class, () -> workgroupService.restoreDeletedWorkgroup(workgroupIdString));
        verify(workgroupRepository, times(1)).findById(workgroupId);
    }



    @Test
    void testAddUserToWorkgroup_NonexistentWorkgroup_ThrowsWorkgroupNotExistException() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkgroupNotExistException.class, () -> workgroupService.addUserToWorkgroup(workgroupmembersDto));
        verify(userRepository, times(1)).findById(userId);
        verify(workgroupRepository, times(1)).findById(workgroupId);
        verifyNoMoreInteractions(workgroupMembersRepository);
    }

    @Test
    void testAddUserToWorkgroup_UserAlreadyAddedToWorkgroup_ThrowsUserAlreadyAddedToWorkgroupException() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        List<Integer> existingUsers = List.of(1, 2, 3); // Existing users in the workgroup

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(new Workgroup()));
        when(workgroupMembersRepository.getAllUserIdFromWorkgroup(workgroupId)).thenReturn(existingUsers);

        // Act & Assert
        assertThrows(UserAlreadyAddedToWorkgroupException.class, () -> workgroupService.addUserToWorkgroup(workgroupmembersDto));
        verify(userRepository, times(1)).findById(userId);
        verify(workgroupRepository, times(1)).findById(workgroupId);
        verify(workgroupMembersRepository, times(1)).getAllUserIdFromWorkgroup(workgroupId);
        verifyNoMoreInteractions(workgroupMembersRepository);
    }


    @Test
    void testAddUserToWorkgroup_ValidUserAndWorkgroup_AddsUserToWorkgroup() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        User user = new User();
        Workgroup workgroup = new Workgroup();
        List<Integer> existingUsers = new ArrayList<>();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));
        when(workgroupMembersRepository.getAllUserIdFromWorkgroup(workgroupId)).thenReturn(existingUsers);
        when(workgroupMembersMapper.fromDtoToEntity(workgroupmembersDto)).thenReturn(new Workgroupmembers());
        when(workgroupMembersRepository.save(any(Workgroupmembers.class))).thenReturn(new Workgroupmembers());

        // Act
        workgroupService.addUserToWorkgroup(workgroupmembersDto);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(workgroupRepository, times(1)).findById(workgroupId);
        verify(workgroupMembersRepository, times(1)).getAllUserIdFromWorkgroup(workgroupId);
        verify(workgroupMembersRepository, times(1)).save(any(Workgroupmembers.class));
    }


    @Test
    void testRemoveUserFromWorkgroup_ValidUserAndWorkgroup_RemovesUserFromWorkgroup() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        User user = new User();
        Workgroup workgroup = new Workgroup();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.of(workgroup));

        // Act
        workgroupService.removeUserFromWorkgroup(workgroupmembersDto);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(workgroupRepository, times(1)).findById(workgroupId);
        verify(workgroupMembersRepository, times(1)).removeUserFromWorkgroup(workgroupId, userId);
    }

    @Test
    void testRemoveUserFromWorkgroup_UserNotExist_ThrowsUserNotExistException() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotExistException.class, () -> {
            workgroupService.removeUserFromWorkgroup(workgroupmembersDto);
        });
    }

    @Test
    void testRemoveUserFromWorkgroup_WorkgroupNotExist_ThrowsWorkgroupNotExistException() {
        // Arrange
        Integer userId = 1;
        Integer workgroupId = 1;

        WorkgroupmembersDto workgroupmembersDto = new WorkgroupmembersDto();
        workgroupmembersDto.setUserId(userId);
        workgroupmembersDto.setWorkgroupId(workgroupId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(workgroupRepository.findById(workgroupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(WorkgroupNotExistException.class, () -> {
            workgroupService.removeUserFromWorkgroup(workgroupmembersDto);
        });
    }










}
