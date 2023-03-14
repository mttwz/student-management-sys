package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupScheduleService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/workgroupschedule")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupScheduleController {
    private final WorkgroupScheduleService workgroupscheduleService;
    private final ResponseFactory responseFactory;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/createworkgroupschedule", consumes = {"application/json"})
    public ResponseEntity<Map> createWorkgroupSchedule(@RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDTO)));

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/deleteworkgroupschedule", consumes = {"application/json"})
    public ResponseEntity<Map> deleteWorkgroupSchedule(@RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", workgroupscheduleService.deleteWorkgroupSchedule(workgroupscheduleDTO)));

    }


    @PostMapping(path = "/uploadfile/{workgroupscheduleId}", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void uploadFile(@PathVariable String workgroupscheduleId,@RequestParam("file") MultipartFile file) throws URISyntaxException, FileNotFoundException {
        workgroupscheduleService.uploadFile(workgroupscheduleId,file);
    }
}
