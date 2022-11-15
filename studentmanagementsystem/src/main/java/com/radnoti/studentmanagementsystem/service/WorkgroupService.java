/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.Workgroup;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author matevoros
 */
@Service
public class WorkgroupService {

    @Autowired
    WorkgroupRepository workgroupRepository;

    JwtUtil jwtUtil = new JwtUtil();

    public void createWorkgroup(String jwt, WorkgroupDTO workgroupDTO) {

        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            workgroupRepository.createWorkgroup(workgroupDTO.getGroupName(), workgroupDTO.getInstitution());
        }

    }

}
