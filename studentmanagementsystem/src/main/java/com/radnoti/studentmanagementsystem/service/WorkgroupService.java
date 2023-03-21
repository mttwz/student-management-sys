/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMembersMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupmembers;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupMembersRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.*;

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


    @Transactional
    public ResponseDto createWorkgroup(WorkgroupDto workgroupDto) {
        if(workgroupDto.getInstitution() == null || workgroupDto.getGroupName()==null || workgroupDto.getInstitution().isEmpty() || workgroupDto.getGroupName().isEmpty()){
            throw new InvalidFormValueException();
        }

        Workgroup workgroup = workgroupMapper.fromDtoToEntity(workgroupDto);
        workgroup.setGroupName(workgroupDto.getGroupName());
        workgroup.setInstitution(workgroupDto.getInstitution());

        Workgroup savedWorkgroup =  workgroupRepository.save(workgroup);

        return new ResponseDto(savedWorkgroup.getId());

    }

    @Transactional
    public void deleteWorkgroup(String workgroupIdString) {
        Integer workgroupId;
        try {
            workgroupId = Integer.parseInt(workgroupIdString);
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }


        Workgroup workgroup = workgroupRepository.findById(workgroupId).orElseThrow(WorkgroupNotExistException::new);

        if(workgroup.getIsDeleted()){
            throw new InvalidFormValueException();
        }

        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        workgroup.setIsDeleted(true);
        workgroup.setDeletedAt(currDate);


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
    public List<UserInfoDto> getUserFromWorkgroup(UserDto userDto) {
        List<UserInfoDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepository.getUserFromWorkgroup(userDto.getId());

        userList.forEach(user -> userDtoList.add(userMapper.fromEntityToInfoDto(user)));

        return userDtoList;
    }





}
