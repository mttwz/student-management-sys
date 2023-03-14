/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
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
     * @param userDto The details of the user in Json format. eg: {
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
    public Integer registerStudent(UserDto userDto){
        if ((userDto.getFirstName() == null || userDto.getLastName() == null|| userDto.getPhone() == null|| userDto.getBirth().toString() == null || userDto.getEmail() == null || userDto.getPassword() == null)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
        }

        if ((userDto.getFirstName().isEmpty() || userDto.getLastName().isEmpty() || userDto.getPhone().isEmpty() || userDto.getBirth().toString().isEmpty() || userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is empty");
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getEmail());

        if(optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }

        Integer savedUserId = studentRepository.registerStudent(userDto.getFirstName(), userDto.getLastName(), userDto.getPhone(), userDto.getBirth(), userDto.getEmail(), userDto.getPassword());

        Optional<User> savedOptionalUser = userRepository.findById(savedUserId);

        if(savedOptionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not saved");
        }
        return savedUserId;
    }

    @Transactional
    public Integer logStudent(StudentDto studentDto){
        // TODO: 2023. 03. 14.  ezt nemtudom meg hogyan :(
        Integer studentId = studentDto.getId();
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
