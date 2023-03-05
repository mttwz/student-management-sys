package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.Role;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDTO;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@RestController
@RequestMapping("/workgroup")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupController {


    private final WorkgroupService workgroupService;
    private final ResponseFactory responseFactory;


    @RolesAllowed({Role.Types.SUPERADMIN, Role.Types.ADMIN})
    @PostMapping(path = "/createworkgroup", consumes = {"application/json"})
    public ResponseEntity<Map> createWorkgroup(@RequestBody WorkgroupDTO workgroupDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", workgroupService.createWorkgroup(workgroupDTO)));

    }


}
