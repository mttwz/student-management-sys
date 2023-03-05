package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.Role;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.service.UserService;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;



    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/registeruser", consumes = {"application/json"})
    public ResponseEntity<Map> adduser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.adduser(userDTO)));
    }

    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/setuserisactivated", consumes = {"application/json"})
    public ResponseEntity<Map> setUserIsActivated(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserIsActivated(userDTO)));
    }

    @RolesAllowed({Role.Types.SUPERADMIN, Role.Types.ADMIN})
    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    public ResponseEntity<Map> deleteUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",userService.deleteUser(userDTO)));
    }

    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    public ResponseEntity<Map> setUserRole(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserRole(userDTO)));
    }

    @RolesAllowed({Role.Types.SUPERADMIN, Role.Types.ADMIN})
    @PostMapping(path = "/addusertoworkgroup", consumes = {"application/json"})
    public ResponseEntity<Map> addUserToWorkgroup(@RequestBody WorkgroupmembersDTO workgroupmembersDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.addUserToWorkgroup(workgroupmembersDTO)));

    }

    @RolesAllowed({Role.Types.SUPERADMIN, Role.Types.ADMIN})
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDTO userDTO) {
        return userService.getWorkgroupScheduleByUserId(authHeader,userDTO);
    }


    @RolesAllowed({Role.Types.SUPERADMIN})
    @GetMapping(path = "/getalluser")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> getWorkgroupScheduleByUserId() {
        return userService.getAllUser();
    }


    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/getuserinfo", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDTO getUserInfo(@RequestBody UserDTO userDTO) {
        return userService.getUserInfo(userDTO);
    }

    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/edituserinfo", consumes = {"application/json"})
    public @ResponseBody ResponseEntity<String> editUserInfo(@RequestBody UserDTO userDTO) {
        userService.editUserInfo(userDTO);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }


    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/searchsuperadmin")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> searchSuperadmin(@RequestBody SearchDTO searchDTO){
        return userService.searchSuperadmin(searchDTO);
    }

    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/getuserfromworkgroup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> getAllUserIdFromWorkgroup(@RequestBody UserDTO userDTO){
        return userService.getUserFromWorkgroup(userDTO);
    }


}
