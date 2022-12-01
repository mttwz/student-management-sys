/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.Student;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.service.StudentService;
import com.radnoti.studentmanagementsystem.service.UserService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    ResponseFactory rf = new ResponseFactory();

    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestHeader("token") String jwt, @RequestBody StudentDTO studentDTO) {
        studentService.connectCardToStudent(jwt, studentDTO);
    }

    @PostMapping(path = "/addstudenttoworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void addStudentToWorkgroup(@RequestHeader("token") String jwt, @RequestBody StudentDTO studentDTO) {
        studentService.addStudentToWorkgroup(jwt, studentDTO);
    }

    @PostMapping(path = "/connectstudenttoUser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectStudentToUser(@RequestHeader("token") String jwt, @RequestBody StudentDTO studentDTO) {
        studentService.connectStudentToUser(jwt, studentDTO);
    }



}
