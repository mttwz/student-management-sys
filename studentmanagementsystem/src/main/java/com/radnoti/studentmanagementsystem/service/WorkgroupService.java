/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.role.RoleNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserAlreadyExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMembersMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupmembers;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupMembersRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class WorkgroupService {
    private final WorkgroupRepository workgroupRepository;
    private final WorkgroupMapper workgroupMapper;

    private final WorkgroupMembersMapper workgroupMembersMapper;

    private final UserMapper userMapper;
    private final WorkgroupMembersRepository workgroupMembersRepository;
    private final UserRepository userRepository;

    private final IdValidatorUtil idValidatorUtil;


    @Transactional
    public ResponseDto createWorkgroup(WorkgroupDto workgroupDto) {
        if(workgroupDto.getInstitution() == null || workgroupDto.getGroupName()==null || workgroupDto.getInstitution().isEmpty() || workgroupDto.getGroupName().isEmpty()){
            throw new FormValueInvalidException();
        }
        ZonedDateTime currDate = java.time.ZonedDateTime.now();


        Workgroup workgroup = workgroupMapper.fromDtoToEntity(workgroupDto);
        workgroup.setGroupName(workgroupDto.getGroupName());
        workgroup.setInstitution(workgroupDto.getInstitution());
        workgroup.setIsDeleted(false);
        workgroup.setCreatedAt(currDate);

        Workgroup savedWorkgroup =  workgroupRepository.save(workgroup);

        return new ResponseDto(savedWorkgroup.getId());

    }

    @Transactional
    public void deleteWorkgroup(String workgroupIdString) {
        Integer workgroupId = idValidatorUtil.idValidator(workgroupIdString);

        Workgroup workgroup = workgroupRepository.findById(workgroupId).orElseThrow(WorkgroupNotExistException::new);

        if(workgroup.getIsDeleted()){
            throw new WorkgroupAlreadyDeletedException();
        }

        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        workgroup.setIsDeleted(true);
        workgroup.setDeletedAt(currDate);


    }

    @Transactional
    public void restoreDeletedWorkgroup(String workgroupIdString) {
        Integer workgroupId = idValidatorUtil.idValidator(workgroupIdString);

        Workgroup workgroup = workgroupRepository.findById(workgroupId)
                .orElseThrow(WorkgroupNotExistException::new);

        if(!workgroup.getIsDeleted()){
            throw new WorkgroupNotDeletedException();
        }

        workgroup.setIsDeleted(false);
        workgroup.setDeletedAt(null);


    }


    /**
     * Adds a student to a workgroup in the database based on the provided DTO object.
     *
     * @return the ID of the created connection.
     * @param workgroupmembersDto The DTO object containing the student's id and the workgroup's id.
     * @throws UserNotExistException if the provided student's ID does not exist in the database.
     * @throws WorkgroupNotExistException if the provided workgroup's ID does not exist in the database.
     *
     */

    @Transactional
    public ResponseDto addUserToWorkgroup(WorkgroupmembersDto workgroupmembersDto) {
        userRepository.findById(workgroupmembersDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        workgroupRepository.findById(workgroupmembersDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        Workgroupmembers workgroupmembers = workgroupMembersMapper.fromDtoToEntity(workgroupmembersDto);
        Workgroupmembers savedWorkgroupmembers =  workgroupMembersRepository.save(workgroupmembers);

        return new ResponseDto( savedWorkgroupmembers.getId());

    }

    @Transactional
    public PagingDto getAllWorkgroup(Pageable pageable) {
        Page<Workgroup> workgroupPage=  workgroupRepository.getAllWorkgroup(pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(workgroupPage.getTotalPages());
        pagingDto.setWorkgroupDtoList(workgroupPage.stream().map(workgroupMapper::fromEntityToDto).toList());
        return pagingDto;
    }


    @Transactional
    public WorkgroupInfoDto getWorkgroupInfo(String workgroupIdString) {
        Integer workgroupId = idValidatorUtil.idValidator(workgroupIdString);

        Workgroup workgroup = workgroupRepository.findById(workgroupId)
                .orElseThrow(WorkgroupNotExistException::new);

        return workgroupMapper.fromEntityToInfoDto(workgroup);
    }

    @Transactional
    public ResponseDto editWorkgroupInfo(String workgroupIdString, WorkgroupInfoDto workgroupInfoDto) {

        Integer workgroupId = idValidatorUtil.idValidator(workgroupIdString);

        Workgroup workgroup = workgroupRepository.findById(workgroupId)
                .orElseThrow(WorkgroupNotExistException::new);


        workgroup.setGroupName(workgroupInfoDto.getGroupName());
        workgroup.setInstitution(workgroupInfoDto.getInstitution());


        return new ResponseDto(workgroupId);
    }


}
