/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author matevoros
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    JwtUtil jwtUtil = new JwtUtil();


    public String updateJwt(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        String jwt = "";
        if (optionalUser.isPresent()) {
            userDTO.setId(optionalUser.get().getId());
            userDTO.setRoleName(optionalUser.get().getRoleId().getRoleType());
            jwt = jwtUtil.generateJwt(userDTO);
            //userRepository.updateJwt(optionalUser.get().getId(), jwt);
        }
        //optionalUser.ifPresent(u -> userRepository.updateJwt(u.getId(), jwtUtil.generateJwt(u.getId(), u.getRoleId().getRoleType())));
        return jwt;
    }

    public void register(String jwt, UserDTO userDTO) {


        if (jwtUtil.roleCheck("Superadmin", jwt)) {
            userRepository.register(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());

        }


    }

    public void registerStudent(UserDTO userDTO) throws NoSuchAlgorithmException {
        System.out.println(userDTO.getPassword());



        userRepository.registerStudent(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());


    }


    public UserLoginDTO login(UserDTO userDTO) throws NoSuchAlgorithmException {




        UserLoginDTO userLoginDTO = new UserLoginDTO();



        int userId = userRepository.login(userDTO.getEmail(), userDTO.getPassword());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userDTO.setId(userId);
            userDTO.setRoleName(optionalUser.get().getRoleId().getRoleType());
            userLoginDTO.setJwt(jwtUtil.generateJwt(userDTO));
            userLoginDTO.setEmail(userDTO.getEmail());

            userLoginDTO.setId(userId);
        }
        return userLoginDTO;


    }

    public void setUserIsActivated(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsActivated(userDTO.getId());

        }


    }

    public void deleteUser(String jwt, UserDTO userDTO) {


        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsDeleted(userDTO.getId());

        }


    }

    public void setUserRole(String jwt, UserDTO userDTO) {


        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());

        }


    }
    public boolean validateJwt(UserDTO userDTO) {
        System.out.println(userDTO.getJwt());
        return jwtUtil.validateJwt(userDTO.getJwt());


    }

}
