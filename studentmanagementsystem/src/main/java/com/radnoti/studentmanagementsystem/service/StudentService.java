/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
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
     * Registers a student based on the provided UserDto object.
     *
     * @param userDto The UserDto object containing the student's information.
     * @return A ResponseDto object containing the ID of the registered student.
     * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
     * @throws FormValueInvalidException If the password in the UserDto object is null or empty.
     * @throws UserNotExistException If the user with the provided ID does not exist.
     */
    @Transactional
    public ResponseDto registerStudent(UserDto userDto) throws NoSuchAlgorithmException {
        userDto.setRoleName(RoleEnum.Types.STUDENT);

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()){
            throw new FormValueInvalidException();
        }

        ResponseDto savedUserIdResponse = userService.adduser(userDto);

        Student student = new Student();
        User user = userRepository.findById(savedUserIdResponse.getId())
                .orElseThrow(UserNotExistException::new);
        student.setUserId(user);

        studentRepository.save(student);
        return new ResponseDto(savedUserIdResponse.getId());
    }


}
