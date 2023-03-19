package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/register-user", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> adduser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", userService.adduser(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-is-activated", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> setUserIsActivated(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", userService.setUserIsActivated(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/delete-user", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> deleteUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id",userService.deleteUser(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-role", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> setUserRole(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", userService.setUserRole(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/add-user-to-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> addUserToWorkgroup(@RequestBody WorkgroupmembersDto workgroupmembersDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", userService.addUserToWorkgroup(workgroupmembersDto)));

    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-user", consumes = {"application/json"}, produces = {"application/json"})
    public  ResponseEntity<ArrayList<UserInfoDto>> getWorkgroupScheduleByUserId() {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(userService.getAllUser());

    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-user-info", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(userService.getUserInfo(userDto));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-user-info/{userId}", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ResponseEntity<Map> editUserInfo(@PathVariable String userId, @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", userService.editUserInfo(userId,userInfoDto)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/search-super-admin", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ArrayList<UserDto> searchSuperadmin(@RequestBody SearchDto searchDto){
        return userService.searchSuperadmin(searchDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-user-from-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ArrayList<UserDto> getAllUserIdFromWorkgroup(@RequestBody UserDto userDto){
        return userService.getUserFromWorkgroup(userDto);
    }


}
