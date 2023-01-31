/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import com.radnoti.studentmanagementsystem.repository.UserRepository;


import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    private final JwtConfig jwtConfig;


    private final DateFormatUtil dateFormatUtil;

    @Transactional
    public void adduser(UserDTO userDTO) {
        userRepository.register(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
    }

    @Transactional
    public ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(String authHeader, UserDTO userDTO) {
        ArrayList<ArrayList<String>> workgroupScheduleList;
        if (jwtConfig.getRoleFromJwt(authHeader).equals("admin")){
            System.out.println("asd");
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtConfig.getIdFromJwt(authHeader));
        }else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDTO.getId());
        }

        ArrayList<WorkgroupscheduleDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < workgroupScheduleList.size(); i++) {
            WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO();
            workgroupscheduleDTO.setId(Integer.parseInt(workgroupScheduleList.get(i).get(0)));
            workgroupscheduleDTO.setName(workgroupScheduleList.get(i).get(1));
            workgroupscheduleDTO.setStart(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(2)));
            workgroupscheduleDTO.setEnd(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(3)));
            workgroupscheduleDTO.setIsOnsite(Boolean.valueOf(workgroupScheduleList.get(i).get(4)));
            dtoList.add(workgroupscheduleDTO);
        }
        return dtoList;
    }

    @Transactional
    public void addUserToWorkgroup(WorkgroupmembersDTO workgroupmembersDTO) {

        userRepository.addUserToWorkgroup(workgroupmembersDTO.getUserId(), workgroupmembersDTO.getWorkgroupId());

    }

    @Transactional
    public Optional<User> getUserData(UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getId());
        return user;
    }

    @Transactional
    public void setUserIsActivated(UserDTO userDTO) {

        userRepository.setUserIsActivated(userDTO.getId());

    }

    @Transactional
    public void deleteUser(UserDTO userDTO) {
        userRepository.setUserIsDeleted(userDTO.getId());
    }

    @Transactional
    public void setUserRole(UserDTO userDTO) {

        userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());

    }


}
