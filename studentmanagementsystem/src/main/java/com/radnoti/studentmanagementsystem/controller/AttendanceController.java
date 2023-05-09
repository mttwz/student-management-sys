package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping(path = "/log-student", consumes = {"text/plain"})
    public ResponseDto logStudent(@RequestBody String cardHash){
        return attendanceService.logStudent(cardHash);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-attendance")
    public void createAttendance(@RequestBody AttendanceDto attendanceDto){
        attendanceService.createAttendance(attendanceDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-attendance")
    public void editAttendance(@RequestBody AttendanceDto attendanceDto){
        attendanceService.editAttendance(attendanceDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-attendance/{attendanceId}")
    public void deleteAttendance(@PathVariable String attendanceId){
        attendanceService.deleteAttendance(attendanceId);
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-attendance-by-user/{userId}")
    public PagingDto getAttendanceByUserId(@PathVariable String userId, Pageable pageable){
        return attendanceService.getAttendanceByUserId(userId,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-attendance-by-student/{studentId}")
    public PagingDto getAttendanceByStudentId(@PathVariable String studentId, Pageable pageable){
        return attendanceService.getAttendanceByStudentId(studentId,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-daily-attendance-by-user-id")
    public List<AttendanceDto> getDailyAttendanceByUserId(@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable){

        return attendanceService.getAttendancePerDayByUserId(userScheduleInfoDto,pageable);
    }

}
