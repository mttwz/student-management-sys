/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMapper;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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


    @Transactional
    public Integer createWorkgroup(WorkgroupDto workgroupDto) {
        if(workgroupDto.getInstitution() == null || workgroupDto.getGroupName()==null || workgroupDto.getInstitution().isEmpty() || workgroupDto.getGroupName().isEmpty()){
            throw new InvalidFormValueException();
        }

        Workgroup workgroup = workgroupMapper.fromDtoToEntity(workgroupDto);
        workgroup.setGroupName(workgroupDto.getGroupName());
        workgroup.setInstitution(workgroupDto.getInstitution());

        Workgroup savedWorkgroup =  workgroupRepository.save(workgroup);

        return savedWorkgroup.getId();

    }





}
