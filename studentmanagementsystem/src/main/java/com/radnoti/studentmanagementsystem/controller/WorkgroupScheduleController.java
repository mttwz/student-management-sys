package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.WorkgroupScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/workgroupschedule")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupScheduleController {
    private final WorkgroupScheduleService workgroupscheduleService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN,RoleEnum.Types.STUDENT})
    @GetMapping(path = "/get-workgroup-schedule-by-user-id/{userId}")
    public PagingDto getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String userId,Pageable pageable) {
        return workgroupscheduleService.getWorkgroupScheduleByUserId(authHeader,userId,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-daily-workgroup-schedule-by-workgroup-id")
    public PagingDto getWorkgroupScheduleByWorkgroupId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody WorkgroupscheduleDto workgroupscheduleDto,Pageable pageable) {
        return workgroupscheduleService.getWorkgroupScheduleByWorkgroupId(authHeader,workgroupscheduleDto,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup-schedule", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto createWorkgroupSchedule(@RequestBody WorkgroupscheduleDto workgroupscheduleDto){
        return workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDto);

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @DeleteMapping(path = "/delete-workgroup-schedule/{workgroupScheduleId}")
    public void deleteWorkgroupSchedule(@PathVariable String workgroupScheduleId) {
        workgroupscheduleService.deleteWorkgroupSchedule(workgroupScheduleId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/restore-deleted-workgroup-schedule/{workgroupScheduleId}")
    public void restoreDeletedWorkgroupSchedule(@PathVariable String workgroupScheduleId) {
        workgroupscheduleService.restoreDeletedWorkgroupSchedule(workgroupScheduleId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-user-schedule")
    public List<UserScheduleInfoDto> getUserSchedule(@RequestBody UserScheduleInfoDto userScheduleInfoDto,Pageable pageable) {
        return workgroupscheduleService.getUserSchedule(userScheduleInfoDto,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN, RoleEnum.Types.STUDENT})
    @PostMapping(path = "/get-own-user-schedule")
    public PagingDto getOwnUserSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,@RequestBody UserScheduleInfoDto userScheduleInfoDto,Pageable pageable) {
        return workgroupscheduleService.getOwnUserSchedule(authHeader,userScheduleInfoDto,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-user-schedule-in-workgroup")
    public List<UserScheduleInfoDto> getUserScheduleInWorkgroup(@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {
        return workgroupscheduleService.gerUserScheduleInWorkgroup(userScheduleInfoDto,pageable);
    }

}
