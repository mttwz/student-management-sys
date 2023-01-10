package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupscheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final WorkgroupscheduleService workgroupscheduleService;

    public AdminController(WorkgroupscheduleService workgroupscheduleService) {
        this.workgroupscheduleService = workgroupscheduleService;
    }

    @PostMapping(path = "/createworkgroupschedule", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroupSchedule(@RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        workgroupscheduleService.createWorkgroupSchedule(workgroupscheduleDTO);
    }


    @PostMapping(path = "/uploadfile", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("workgroupscheduleId") Integer workgroupscheduleId) {
        WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO(workgroupscheduleId);
        workgroupscheduleService.uploadFile(file,workgroupscheduleDTO);
    }


}
