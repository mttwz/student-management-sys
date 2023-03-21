package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDto;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
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


    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> createWorkgroup(@RequestBody WorkgroupDto workgroupDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(Map.of("id", workgroupService.createWorkgroup(workgroupDto)));

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/delete-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public void deleteWorkgroup(@RequestBody WorkgroupDto workgroupDto) {
        workgroupService.deleteWorkgroup(workgroupDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", workgroupService.addUserToWorkgroup(workgroupmembersDto)));

    }


}
