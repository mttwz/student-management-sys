package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import com.radnoti.studentmanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    @PostMapping(path = "/log-student", consumes = {"text/plain"})
    public ResponseDto logStudent(@RequestBody String cardHash){
        return attendanceService.logStudent(cardHash);
    }

    @GetMapping(path = "/get-attendance-by-user/{userId}")
    public PagingDto getAttendanceByUserId(@PathVariable String userId, Pageable pageable){
        return attendanceService.getAttendanceByUserId(userId,pageable);
    }
    @GetMapping(path = "/get-attendance-by-student/{studentId}")
    public PagingDto getAttendanceByStudentId(@PathVariable String studentId, Pageable pageable){
        return attendanceService.getAttendanceByStudentId(studentId,pageable);
    }
    @GetMapping(path = "/get-daily-attendance-by-user-id")
    public List<AttendanceDto> getDailyAttendanceByUserId(@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable){
        return attendanceService.getAttendancePerDayByUserId(userScheduleInfoDto,pageable);
    }

}
