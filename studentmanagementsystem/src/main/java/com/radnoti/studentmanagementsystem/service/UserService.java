/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author matevoros
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private DateFormatUtil dateFormatUtil;

    @Transactional
    public String updateJwt(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        String jwt = "";
        if (optionalUser.isPresent()) {
            userDTO.setId(optionalUser.get().getId());
            userDTO.setRoleName(optionalUser.get().getRoleId().getRoleType());
            jwt = jwtUtil.generateJwt(optionalUser);
            //userRepository.updateJwt(optionalUser.get().getId(), jwt);
        }
        //optionalUser.ifPresent(u -> userRepository.updateJwt(u.getId(), jwtUtil.generateJwt(u.getId(), u.getRoleId().getRoleType())));
        return jwt;
    }
    @Transactional
    public void register(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt)) {
            userRepository.register(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
        }
    }
    @Transactional
    public void registerStudent(UserDTO userDTO){
        userRepository.registerStudent(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
    }

    @Transactional
    public UserDTO.UserLoginDTO login(UserDTO userDTO){
        UserDTO.UserLoginDTO userLoginDTO = new UserDTO.UserLoginDTO();
        int userId = userRepository.login(userDTO.getEmail(), userDTO.getPassword());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userLoginDTO.setId(userId);
            userLoginDTO.setEmail(optionalUser.get().getEmail());
            userLoginDTO.setJwt(jwtUtil.generateJwt(optionalUser));
            userLoginDTO.setFirstName(optionalUser.get().getFirstName());
            userLoginDTO.setLastName(optionalUser.get().getLastName());
        }
        return userLoginDTO;
    }

    @Transactional
    public Optional<User> getUserData(UserDTO userDTO){
        Optional<User> user = userRepository.findById(userDTO.getId());
        return user;
    }

    @Transactional
    public void setUserIsActivated(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsActivated(userDTO.getId());
        }
    }
    @Transactional
    public void deleteUser(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsDeleted(userDTO.getId());
        }
    }
    @Transactional
    public void setUserRole(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());
        }
    }


    //ezt nemtudom miert csináltam
    @Transactional
    public Map validateJwt(UserDTO userDTO) {
        HashMap<String, String> map = new HashMap<>();
        boolean isValid = jwtUtil.validateJwt(userDTO.getJwt());
        map.put("valid", String.valueOf(isValid));
        return map;
    }

    @Transactional
    public void addUserToWorkgroup(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.addUserToWorkgroup(userDTO.getId(), userDTO.getWorkgroupId());
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
