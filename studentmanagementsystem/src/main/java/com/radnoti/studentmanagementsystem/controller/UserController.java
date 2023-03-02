package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.service.UserService;
import com.radnoti.studentmanagementsystem.service.WorkgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WorkgroupService workgroupService;


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/registeruser", consumes = {"application/json"})
    public ResponseEntity<String> adduser(@RequestBody UserDTO userDTO) {
        userService.adduser(userDTO);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/setuserisactivated", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserIsActivated(@RequestBody UserDTO userDTO) {
        userService.setUserIsActivated(userDTO);
    }
    @RolesAllowed({"SUPERADMIN","ADMIN"})
    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteUser(userDTO);
    }

    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserRole(@RequestBody UserDTO userDTO) {
        userService.setUserRole(userDTO);
    }

    @RolesAllowed({"SUPERADMIN","ADMIN"})
    @PostMapping(path = "/addusertoworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void addUserToWorkgroup(@RequestBody WorkgroupmembersDTO workgroupmembersDTO) {
        userService.addUserToWorkgroup(workgroupmembersDTO);
    }

    @RolesAllowed({"SUPERADMIN","ADMIN"})
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDTO userDTO) {
        return userService.getWorkgroupScheduleByUserId(authHeader,userDTO);
    }


    @RolesAllowed({"SUPERADMIN"})
    @GetMapping(path = "/getalluser")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> getWorkgroupScheduleByUserId() {
        return userService.getAllUser();
    }


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/getuserinfo", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDTO getUserInfo(@RequestBody UserDTO userDTO) {
        return userService.getUserInfo(userDTO);
    }

    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/edituserinfo", consumes = {"application/json"})
    public @ResponseBody ResponseEntity<String> editUserInfo(@RequestBody UserDTO userDTO) {
        userService.editUserInfo(userDTO);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }


    @RolesAllowed(("SUPERADMIN"))
    @PostMapping(path = "/searchsuperadmin")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> searchSuperadmin(@RequestBody SearchDTO searchDTO){
        return userService.searchSuperadmin(searchDTO);
    }

    @RolesAllowed(("SUPERADMIN"))
    @PostMapping(path = "/getuserfromworkgroup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> getAllUserIdFromWorkgroup(@RequestBody UserDTO userDTO){
        return userService.getUserFromWorkgroup(userDTO);
    }


}
