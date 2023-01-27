package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/workgroup")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupController {


    private final WorkgroupService workgroupService;


    @RolesAllowed({"SUPERADMIN","ADMIN"})
    @PostMapping(path = "/createworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroup(@RequestBody WorkgroupDTO workgroupDTO) {
        workgroupService.createWorkgroup(workgroupDTO);

    }
}
