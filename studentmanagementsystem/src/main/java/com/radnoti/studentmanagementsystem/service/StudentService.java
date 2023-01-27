/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    private final JwtConfig jwtConfig;

    private final DateFormatUtil dateFormatUtil;

    @Transactional
    public void registerStudent(UserDTO userDTO){
        studentRepository.registerStudent(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
    }

    @Transactional
    public void connectStudentToUser(StudentDTO studentDTO) {

        studentRepository.connectStudentToUser(studentDTO.getId(), studentDTO.getUserId());

    }




}
