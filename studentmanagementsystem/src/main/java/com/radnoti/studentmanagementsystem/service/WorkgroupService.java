/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
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


    @Transactional
    public Integer createWorkgroup(WorkgroupDTO workgroupDTO) {
        Integer workgroupId = workgroupRepository.createWorkgroup(workgroupDTO.getGroupName(), workgroupDTO.getInstitution());
        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupId);

        if (optionalWorkgroup.isEmpty() || !Objects.equals(optionalWorkgroup.get().getGroupName(), workgroupDTO.getGroupName()) || !Objects.equals(optionalWorkgroup.get().getInstitution(), workgroupDTO.getInstitution())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Workgroup not created");
        }
        return workgroupId;

    }





}
