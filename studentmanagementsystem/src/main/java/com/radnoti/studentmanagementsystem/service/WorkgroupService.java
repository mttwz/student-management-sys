/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import com.sun.xml.bind.v2.runtime.CompositeStructureBeanInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class WorkgroupService {


    private final WorkgroupRepository workgroupRepository;

    private final  JwtConfig jwtConfig;

    @Transactional
    public void createWorkgroup(WorkgroupDTO workgroupDTO) {
        workgroupRepository.createWorkgroup(workgroupDTO.getGroupName(), workgroupDTO.getInstitution());
    }





}
