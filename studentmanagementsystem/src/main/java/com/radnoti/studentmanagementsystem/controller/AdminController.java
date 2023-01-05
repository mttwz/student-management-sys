package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupscheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final WorkgroupscheduleService workgroupscheduleService;


    public AdminController(WorkgroupscheduleService workgroupscheduleService) {
        this.workgroupscheduleService = workgroupscheduleService;
    }

    @PostMapping(path = "/createworkgroupschedule", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroupSchedule(@RequestHeader("Authorization") String jwt, @RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        workgroupscheduleService.createWorkgroupSchedule(jwt, workgroupscheduleDTO);
    }
}
