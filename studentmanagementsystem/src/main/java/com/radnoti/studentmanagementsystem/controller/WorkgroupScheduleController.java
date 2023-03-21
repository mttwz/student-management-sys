package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.service.WorkgroupScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workgroupschedule")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupScheduleController {
    private final WorkgroupScheduleService workgroupscheduleService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-workgroup-schedule-by-user-id", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<List<WorkgroupscheduleDto>> getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(workgroupscheduleService.getWorkgroupScheduleByUserId(authHeader,userDto));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup-schedule", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto createWorkgroupSchedule(@RequestBody WorkgroupscheduleDto workgroupscheduleDto) throws ParseException {
        return workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDto);

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/delete-workgroup-schedule", consumes = {"application/json"}, produces = {"application/json"})
    public void deleteWorkgroupSchedule(@RequestBody WorkgroupscheduleDto workgroupscheduleDto) {
        workgroupscheduleService.deleteWorkgroupSchedule(workgroupscheduleDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/restore-deleted-workgroup-schedule", consumes = {"application/json"}, produces = {"application/json"})
    public void restoreDeletedWorkgroupSchedule(@RequestBody WorkgroupscheduleDto workgroupscheduleDto) {
        workgroupscheduleService.restoreDeletedWorkgroupSchedule(workgroupscheduleDto);
    }

}
