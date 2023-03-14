/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.StudentService;

import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/student")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ResponseFactory responseFactory;



    @PostMapping(path = "/register-student", consumes = {"application/json"})
    public ResponseEntity<Map> register(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(responseFactory.createResponse("id", studentService.registerStudent(userDto)));
    }



}
