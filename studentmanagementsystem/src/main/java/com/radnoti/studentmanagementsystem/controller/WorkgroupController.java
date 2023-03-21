package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workgroup")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class WorkgroupController {


    private final WorkgroupService workgroupService;


    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/create-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto createWorkgroup(@RequestBody WorkgroupDto workgroupDto) {
        return workgroupService.createWorkgroup(workgroupDto);

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-workgroup/{workgroupId}")
    public void deleteWorkgroup(@PathVariable String workgroupId) {
        workgroupService.deleteWorkgroup(workgroupId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return workgroupService.addUserToWorkgroup(workgroupmembersDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-user-from-workgroup")
    public @ResponseBody List<UserInfoDto> getAllUserIdFromWorkgroup(@RequestBody UserDto userDto){
        return workgroupService.getUserFromWorkgroup(userDto);
    }


}
