package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.model.WorkgroupSchedule;
import com.radnoti.studentmanagementsystem.service.WorkgroupscheduleService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(path = "/workgroupschedule")
public class WorkgroupscheduleController {

    @Autowired
    private WorkgroupscheduleService workgroupscheduleService;

    ResponseFactory rf = new ResponseFactory();

    @PostMapping(path = "/createworkgroupschedule", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroupSchedule(@RequestHeader("token") String jwt, @RequestBody WorkgroupscheduleDTO workgroupscheduleDTO) {
        workgroupscheduleService.createWorkgroupSchedule(jwt, workgroupscheduleDTO);
    }

    //Teszt


}
