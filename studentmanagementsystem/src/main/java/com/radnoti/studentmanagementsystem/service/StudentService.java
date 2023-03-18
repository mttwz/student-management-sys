/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotSavedException;
import com.radnoti.studentmanagementsystem.exception.user.UserAlreadyExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotSavedException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserService userService;

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;


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
    public Integer registerStudent(UserDto userDto) throws NoSuchAlgorithmException {
        userDto.setRoleName(RoleEnum.Types.STUDENT);
        Integer savedUserId = userService.adduser(userDto);
        Student student = new Student();
        User user = userRepository.findById(savedUserId)
                .orElseThrow(UserNotExistException::new);
        student.setUserId(user);
        studentRepository.save(student);
        return savedUserId;
    }


}
