/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.Card;
import com.radnoti.studentmanagementsystem.model.Student;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author matevoros
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    JwtUtil jwtUtil = new JwtUtil();
    DateFormatUtil dateFormatUtil = new DateFormatUtil();

    @Transactional
    public void connectCardToStudent(String jwt, StudentDTO studentDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
/*            Optional<Student> student = studentRepository.findById(studentDTO.getId());
            if (student.isPresent()){
                student.get().setCardId(new Card(studentDTO.getCardId()));
                studentRepository.save(student.get());
            }*/
            studentRepository.connectCardToStudent(studentDTO.getId(), studentDTO.getCardId());
        }
    }

    @Transactional
    public void connectStudentToUser(String jwt, StudentDTO studentDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            studentRepository.connectStudentToUser(studentDTO.getId(), studentDTO.getUserId());
        }
    }



}
