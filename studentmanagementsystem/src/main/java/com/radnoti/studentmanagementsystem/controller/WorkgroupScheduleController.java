package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupscheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/workgroupschedule")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupScheduleController {
    private final WorkgroupscheduleService workgroupscheduleService;

    @RolesAllowed({"SUPERADMIN","ADMIN"})
    @PostMapping(path = "/createworkgroupschedule", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroupSchedule(@RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        System.out.println(workgroupscheduleDTO);
        workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDTO);
    }


    @PostMapping(path = "/uploadfile", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("workgroupscheduleId") Integer workgroupscheduleId) {
        WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO(workgroupscheduleId);
        workgroupscheduleService.uploadFile(file,workgroupscheduleDTO);
    }
}
