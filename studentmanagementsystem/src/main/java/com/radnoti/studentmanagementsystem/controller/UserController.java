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
    @PostMapping(path = "/register-user", consumes = {"application/json"})
    public ResponseEntity<Map> adduser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.adduser(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-is-activated", consumes = {"application/json"})
    public ResponseEntity<Map> setUserIsActivated(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserIsActivated(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/delete-user", consumes = {"application/json"})
    public ResponseEntity<Map> deleteUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",userService.deleteUser(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-role", consumes = {"application/json"})
    public ResponseEntity<Map> setUserRole(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.setUserRole(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"})
    public ResponseEntity<Map> addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.addUserToWorkgroup(workgroupmembersDto)));

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/get-workgroup-schedule-by-user-id", consumes = {"application/json"})
    public ResponseEntity<ArrayList<WorkgroupscheduleDto>> getWorkgroupScheduleByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getWorkgroupScheduleByUserId(authHeader,userDto));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-user")
    public  ResponseEntity<ArrayList<UserInfoDto>> getWorkgroupScheduleByUserId() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getAllUser());

    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-user-info", consumes = {"application/json"})
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(userService.getUserInfo(userDto));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-user-info/{userId}", consumes = {"application/json"})
    public @ResponseBody ResponseEntity<Map> editUserInfo(@PathVariable String userId, @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", userService.editUserInfo(userId,userInfoDto)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/search-super-admin")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDto> searchSuperadmin(@RequestBody SearchDto searchDto){
        return userService.searchSuperadmin(searchDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-user-from-workgroup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<UserDto> getAllUserIdFromWorkgroup(@RequestBody UserDto userDto){
        return userService.getUserFromWorkgroup(userDto);
    }


}
