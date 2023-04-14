package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/restore-deleted-workgroup/{workgroupId}")
    public void restoreDeleteWorkgroup(@PathVariable String workgroupId) {
        workgroupService.restoreDeletedWorkgroup(workgroupId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseDto addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return workgroupService.addUserToWorkgroup(workgroupmembersDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-all-workgroups")
    public PagingDto getAllWorkgroups(Pageable pageable) {
        return workgroupService.getAllWorkgroup(pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-workgroup-info/{workgroupId}")
    public ResponseEntity<WorkgroupInfoDto> getWorkgroupInfo(@PathVariable String workgroupId){
        return ResponseEntity.ok(workgroupService.getWorkgroupInfo(workgroupId));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-workgroup-info/{workgroupId}", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ResponseDto editWorkgroupInfo(@PathVariable String workgroupId, @RequestBody WorkgroupInfoDto workgroupInfoDto) {
        return workgroupService.editWorkgroupInfo(workgroupId,workgroupInfoDto);
    }


//    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
//    @GetMapping(path = "/get-user-from-workgroup")
//    public @ResponseBody List<UserInfoDto> getAllUserIdFromWorkgroup(@RequestBody UserDto userDto){
//        return workgroupService.getUserFromWorkgroup(userDto);
//    }


}
