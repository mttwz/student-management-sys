package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cross-origin}")
public class AttendanceController {

    private final AttendanceService attendanceService;



    /**
     * Logs a student's attendance using their card hash.
     *
     * @param cardHash The card hash of the student.
     * @return The ResponseDto indicating the success of the operation.
     */
    @PostMapping(path = "/log-student", consumes = {"text/plain"})
    public ResponseDto logStudent(@RequestBody String cardHash){
        return attendanceService.logStudent(cardHash);
    }


    /**
     * Creates a new attendance record.
     *
     * @param attendanceDto The AttendanceDto containing the details of the attendance.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-attendance")
    public void createAttendance(@RequestBody AttendanceDto attendanceDto){
        attendanceService.createAttendance(attendanceDto);
    }


    /**
     * Edits an existing attendance record.
     *
     * @param attendanceDto The AttendanceDto containing the updated details of the attendance.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-attendance")
    public void editAttendance(@RequestBody AttendanceDto attendanceDto){
        attendanceService.editAttendance(attendanceDto);
    }



    /**
     *  Deletes an attendance record with the specified ID.
     *      Only users with the SUPERADMIN role are allowed to access this endpoint.
     *      @param attendanceId The ID of the attendance record to be deleted.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-attendance/{attendanceId}")
    public void deleteAttendance(@PathVariable String attendanceId){
        attendanceService.deleteAttendance(attendanceId);
    }


    /**
     *Retrieves the attendance records for a specific user.
     * Only users with the SUPERADMIN role are allowed to access this endpoint.
     * @param userId The ID of the user.
     * @param pageable The pageable information for pagination.
     * @return object containing the attendance records for the user.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-attendance-by-user/{userId}")
    public PagingDto getAttendanceByUserId(@PathVariable String userId, Pageable pageable){
        return attendanceService.getAttendanceByUserId(userId,pageable);
    }


    /**
     * Retrieves the attendance records for a specific student.
     * Only users with the SUPERADMIN role are allowed to access this endpoint.
     * @param studentId The ID of the student.
     * @param pageable The pageable information for pagination.
     * @return  object containing the attendance records for the student.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-attendance-by-student/{studentId}")
    public PagingDto getAttendanceByStudentId(@PathVariable String studentId, Pageable pageable){
        return attendanceService.getAttendanceByStudentId(studentId,pageable);
    }


    /**
     * Retrieves the daily attendance records for a specific user.
     * Users with either the SUPERADMIN or ADMIN roles are allowed to access this endpoint.
     * @param userScheduleInfoDto The information of the user and date for retrieving the attendance records.
     * @param pageable The pageable information for pagination.
     * @return A list of objects containing the daily attendance records for the user.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-daily-attendance-by-user-id")
    public List<AttendanceDto> getDailyAttendanceByUserId(@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable){

        return attendanceService.getAttendancePerDayByUserId(userScheduleInfoDto,pageable);
    }


    /**
     *Retrieves the daily attendance records for the authenticated user.
     * Users with either the SUPERADMIN, ADMIN, or STUDENT roles are allowed to access this endpoint.
     * @param authHeader The Authorization header containing the authentication token.
     * @param userScheduleInfoDto The information of the user and date for retrieving the attendance records.
     * @param pageable The pageable information for pagination.
     * @return  object containing the daily attendance records for the user.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN,RoleEnum.Types.STUDENT})
    @PostMapping(path = "/get-own-daily-attendance")
    public PagingDto getDailyAttendanceByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,@RequestBody UserScheduleInfoDto userScheduleInfoDto, Pageable pageable){
        return attendanceService.getOwnAttendancePerDay(authHeader,userScheduleInfoDto,pageable);
    }

}
