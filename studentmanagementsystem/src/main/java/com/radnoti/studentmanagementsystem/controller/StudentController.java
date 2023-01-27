/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/student")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;



    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/registerstudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void register(@RequestBody UserDTO userDTO){
        studentService.registerStudent(userDTO);
    }

    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/connectstudenttouser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectStudentToUser(@RequestBody StudentDTO studentDTO) {
        studentService.connectStudentToUser(studentDTO);
    }
}
