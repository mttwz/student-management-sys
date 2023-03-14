package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import com.radnoti.studentmanagementsystem.service.StudentService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AttendanceController {


    private final AttendanceService attendanceService;
    private final ResponseFactory responseFactory;

    @PostMapping(path = "/log-student", consumes = {"application/json"})
    public ResponseEntity<Map> logStudent(@RequestBody StudentDto studentDto){
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(responseFactory.createResponse("id", attendanceService.logStudent(studentDto)));
    }
}
