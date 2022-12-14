/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author matevoros
 */
@Service
public class StudentService {


    private final StudentRepository studentRepository;


    private final  JwtUtil jwtUtil;


    private final DateFormatUtil dateFormatUtil;

    public StudentService(StudentRepository studentRepository, JwtUtil jwtUtil, DateFormatUtil dateFormatUtil) {
        this.studentRepository = studentRepository;
        this.jwtUtil = jwtUtil;
        this.dateFormatUtil = dateFormatUtil;
    }


    @Transactional
    public void registerStudent(UserDTO userDTO){
        studentRepository.registerStudent(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
    }




}
