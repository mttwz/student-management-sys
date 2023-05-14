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
@CrossOrigin(origins = "${cross-origin}")
@RequiredArgsConstructor
public class WorkgroupScheduleController {
    private final WorkgroupScheduleService workgroupscheduleService;


    /**
     * Retrieves the workgroup schedule for a specific user.
     *
     * @param authHeader the authorization header.
     * @param userId     the ID of the user.
     * @param pageable   the pageable object for pagination.
     * @return a PagingDto containing the workgroup schedule.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN,RoleEnum.Types.STUDENT})
    @GetMapping(path = "/get-workgroup-schedule-by-user-id/{userId}")
    public PagingDto getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String userId,Pageable pageable) {
        return workgroupscheduleService.getWorkgroupScheduleByUserId(authHeader,userId,pageable);
    }


    /**
     * Retrieves the daily workgroup schedule for a specific workgroup.
     *
     * @param authHeader          the authorization header.
     * @param workgroupscheduleDto the WorkgroupscheduleDto object containing the workgroup ID and date.
     * @param pageable            the pageable object for pagination.
     * @return a PagingDto containing the daily workgroup schedule.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-daily-workgroup-schedule-by-workgroup-id")
    public PagingDto getWorkgroupScheduleByWorkgroupId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody WorkgroupscheduleDto workgroupscheduleDto,Pageable pageable) {
        return workgroupscheduleService.getWorkgroupScheduleByWorkgroupId(authHeader,workgroupscheduleDto,pageable);
    }


    /**
     * Creates a new workgroup schedule.
     *
     * @param workgroupscheduleDto the WorkgroupscheduleDto object containing the workgroup schedule information.
     * @return a ResponseDto containing the response.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup-schedule", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto createWorkgroupSchedule(@RequestBody WorkgroupscheduleDto workgroupscheduleDto){
        return workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDto);

    }


    /**
     * Deletes a workgroup schedule.
     *
     * @param workgroupScheduleId the ID of the workgroup schedule to delete.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @DeleteMapping(path = "/delete-workgroup-schedule/{workgroupScheduleId}")
    public void deleteWorkgroupSchedule(@PathVariable String workgroupScheduleId) {
        workgroupscheduleService.deleteWorkgroupSchedule(workgroupScheduleId);
    }


    /**
     * Restores a deleted workgroup schedule.
     *
     * @param workgroupScheduleId the ID of the workgroup schedule to restore.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/restore-deleted-workgroup-schedule/{workgroupScheduleId}")
    public void restoreDeletedWorkgroupSchedule(@PathVariable String workgroupScheduleId) {
        workgroupscheduleService.restoreDeletedWorkgroupSchedule(workgroupScheduleId);
    }


    /**
     * Retrieves the user schedule based on the provided user schedule information and pageable settings.
     *
     * @param userScheduleInfoDto The user schedule information.
     * @param pageable           The pageable settings for the result.
     * @return The list of user schedule information.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-user-schedule")
    public List<UserScheduleInfoDto> getUserSchedule(@RequestBody UserScheduleInfoDto userScheduleInfoDto,Pageable pageable) {
        return workgroupscheduleService.getUserSchedule(userScheduleInfoDto,pageable);
    }

    /**
     * Retrieves the own user schedule based on the provided user schedule information, authorization header, and pageable settings.
     *
     * @param authHeader          The authorization header.
     * @param userScheduleInfoDto The user schedule information.
     * @param pageable           The pageable settings for the result.
     * @return The paging result of own user schedule.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN, RoleEnum.Types.STUDENT})
    @PostMapping(path = "/get-own-user-schedule")
    public PagingDto getOwnUserSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,@RequestBody UserScheduleInfoDto userScheduleInfoDto,Pageable pageable) {
        return workgroupscheduleService.getOwnUserSchedule(authHeader,userScheduleInfoDto,pageable);
    }

    /**
     * Retrieves the user schedule within a workgroup based on the provided user schedule information and pageable settings.
     *
     * @param userScheduleInfoDto The user schedule information.
     * @param pageable           The pageable settings for the result.
     * @return The list of user schedule information within the workgroup.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-user-schedule-in-workgroup")
    public List<UserScheduleInfoDto> getUserScheduleInWorkgroup(@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {
        return workgroupscheduleService.gerUserScheduleInWorkgroup(userScheduleInfoDto,pageable);
    }

}
