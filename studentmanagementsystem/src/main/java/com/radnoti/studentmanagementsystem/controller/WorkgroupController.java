/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.model.User_;
import com.radnoti.studentmanagementsystem.model.Workgroup;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/workgroup")
public class WorkgroupController {

    @Autowired
    private WorkgroupService workgroupService;

    ResponseFactory rf = new ResponseFactory();

    @PostMapping(path = "/createworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public @ResponseBody void createWorkgroup(@RequestHeader("token") String jwt, @RequestBody WorkgroupDTO workgroupDTO) {
        workgroupService.createWorkgroup(jwt, workgroupDTO);

    }

}
