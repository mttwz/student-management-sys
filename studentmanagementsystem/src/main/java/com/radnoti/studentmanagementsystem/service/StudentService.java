/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;

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
public class StudentService {
    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final DateFormatUtil dateFormatUtil;


    /**
     * Registers a new user with student privileges
     * @param userDTO The details of the user in Json format. eg: {
     *     "firstName" : "userFirstName",
     *     "lastName" : "userLastName",
     *     "phone" : "userPhone",
     *     "birth": "1111-12-12T00:00:00Z",
     *     "email" : "qqq",
     *     "password" : "qqq"
     * }
     * @return saved user id
     */
    @Transactional
    public Integer registerStudent(UserDTO userDTO){
        if ((userDTO.getFirstName() == null || userDTO.getLastName() == null|| userDTO.getPhone() == null|| userDTO.getBirth().toString() == null || userDTO.getEmail() == null || userDTO.getPassword() == null)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
        }

        if (( userDTO.getFirstName().isEmpty() || userDTO.getLastName().isEmpty() || userDTO.getPhone().isEmpty() || userDTO.getBirth().toString().isEmpty() || userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is empty");
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getEmail());

        if(optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }

        Integer savedUserId = studentRepository.registerStudent(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());

        Optional<User> savedOptionalUser = userRepository.findById(savedUserId);

        if(savedOptionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not saved");
        }
        return savedUserId;
    }

    @Transactional
    public Integer logStudent(StudentDTO studentDTO){
        // TODO: 2023. 03. 14.  ezt nemtudom meg hogyan :(
        Integer studentId = studentDTO.getId();
        if(studentId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Value is null");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student not exist");
        }

        Integer connectionId = studentRepository.logStudent(studentId);

        if(connectionId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Log not saved");
        }

        return connectionId;
    }
}
