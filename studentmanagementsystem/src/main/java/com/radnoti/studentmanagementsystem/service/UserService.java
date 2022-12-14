/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import com.radnoti.studentmanagementsystem.repository.UserRepository;


import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author matevoros
 */
@Service
public class UserService {


    private final UserRepository userRepository;


    private final JwtUtil jwtUtil;



    private final DateFormatUtil dateFormatUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, DateFormatUtil dateFormatUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.dateFormatUtil = dateFormatUtil;
    }

    @Transactional
    public void register(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt)) {
            userRepository.register(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
        }
    }

    @Transactional
    public ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(UserDTO userDTO){
        ArrayList<ArrayList<String>> workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDTO.getId());
        ArrayList<WorkgroupscheduleDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < workgroupScheduleList.size(); i++) {
            WorkgroupscheduleDTO wsDTO = new WorkgroupscheduleDTO();
            wsDTO.setId(Integer.parseInt(workgroupScheduleList.get(i).get(0)));
            wsDTO.setName(workgroupScheduleList.get(i).get(1));
            wsDTO.setWorkgroupId(Integer.parseInt(workgroupScheduleList.get(i).get(2)));
            wsDTO.setStart(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(3)));
            wsDTO.setEnd(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(4)));
            wsDTO.setOnsite(Boolean.valueOf(workgroupScheduleList.get(i).get(5)));
            dtoList.add(wsDTO);
        }
        return dtoList;
    }



}
