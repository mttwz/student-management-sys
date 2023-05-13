/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/student")
@CrossOrigin(origins = "${cross-origin}")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * Registers a student.
     *
     * @param userDto the user DTO containing the student details.
     * @return a ResponseDto object containing the registration response.
     * @throws NoSuchAlgorithmException if the hashing algorithm is not found.
     */
    @PostMapping(path = "/register-student", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto register(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return studentService.registerStudent(userDto);
    }



}
