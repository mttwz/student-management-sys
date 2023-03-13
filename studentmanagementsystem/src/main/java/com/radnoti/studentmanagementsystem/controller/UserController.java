package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
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



    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/registeruser", consumes = {"application/json"})
    public ResponseEntity<Map> adduser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.adduser(userDTO)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/setuserisactivated", consumes = {"application/json"})
    public ResponseEntity<Map> setUserIsActivated(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserIsActivated(userDTO)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    public ResponseEntity<Map> deleteUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",userService.deleteUser(userDTO)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    public ResponseEntity<Map> setUserRole(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserRole(userDTO)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/addusertoworkgroup", consumes = {"application/json"})
    public ResponseEntity<Map> addUserToWorkgroup(@RequestBody WorkgroupmembersDTO workgroupmembersDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.addUserToWorkgroup(workgroupmembersDTO)));

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    public ResponseEntity<ArrayList<WorkgroupscheduleDTO>> getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getWorkgroupScheduleByUserId(authHeader,userDTO));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/getalluser")
    public  ResponseEntity<ArrayList<UserInfoDTO>> getWorkgroupScheduleByUserId() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getAllUser());

    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/getuserinfo", consumes = {"application/json"})
    public ResponseEntity<UserInfoDTO> getUserInfo(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getUserInfo(userDTO));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edituserinfo/{userId}", consumes = {"application/json"})
    public @ResponseBody ResponseEntity<Map> editUserInfo(@PathVariable String userId, @RequestBody UserInfoDTO userInfoDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.editUserInfo(userId,userInfoDTO)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/searchsuperadmin")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> searchSuperadmin(@RequestBody SearchDTO searchDTO){
        return userService.searchSuperadmin(searchDTO);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/getuserfromworkgroup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDTO> getAllUserIdFromWorkgroup(@RequestBody UserDTO userDTO){
        return userService.getUserFromWorkgroup(userDTO);
    }


}
