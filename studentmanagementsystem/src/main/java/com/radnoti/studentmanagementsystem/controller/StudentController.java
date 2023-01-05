/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.StudentService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/registerstudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void registerStudent(@RequestBody UserDTO userDTO){
        studentService.registerStudent(userDTO);

    }



}
