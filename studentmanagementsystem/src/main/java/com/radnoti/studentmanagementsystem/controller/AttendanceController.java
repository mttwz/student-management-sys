package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import com.radnoti.studentmanagementsystem.service.StudentService;
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
    @PostMapping(path = "/log-student", consumes = {"application/json"})
    public ResponseEntity<Map> logStudent(@RequestBody StudentDto studentDto){
        return ResponseEntity.ok()
                .header("Content-Type","application/json")
                .body(Map.of("id", attendanceService.logStudent(studentDto)));
    }


}
