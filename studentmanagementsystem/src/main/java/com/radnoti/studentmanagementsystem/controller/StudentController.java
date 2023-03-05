/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.Role;
import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.StudentService;

import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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


    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/registerstudent", consumes = {"application/json"})
    public ResponseEntity<Map> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(responseFactory.createResponse("id", studentService.registerStudent(userDTO)));
    }

//    @RolesAllowed({Role.Types.SUPERADMIN})
//    @PostMapping(path = "/connectstudenttouser", consumes = {"application/json"})
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody void connectStudentToUser(@RequestBody StudentDTO studentDTO) {
//        studentService.connectStudentToUser(studentDTO);
//    }
}
