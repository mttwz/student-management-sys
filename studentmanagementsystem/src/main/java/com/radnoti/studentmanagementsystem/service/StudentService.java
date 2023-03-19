/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserAlreadyExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.security.NoSuchAlgorithmException;


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
     * Adds a new user to the system with student privileges.
     *
     * @param userDto The DTO object representing the user to be added.
     * @return The ID of the added user.
     * @throws InvalidFormValueException if any required field in the provided DTO object is null or empty.
     * @throws UserAlreadyExistException if a user with the provided email already exists in the database.
     * @throws NoSuchAlgorithmException if an error occurs while hashing the user's password.
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
