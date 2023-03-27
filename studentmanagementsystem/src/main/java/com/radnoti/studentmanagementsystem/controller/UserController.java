package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/register-user")
    public ResponseDto adduser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return userService.adduser(userDto);
    }


    @PostMapping(path = "/set-user-is-activated")
    public void setUserIsActivated(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        userService.setUserIsActivated(userDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @DeleteMapping(path = "/delete-user/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/restore-deleted-user/{userId}")
    public void restoreDeletedUser(@PathVariable String userId) {
        userService.restoreDeletedUser(userId);
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-user")
    public  ResponseEntity<List<UserInfoDto>> getWorkgroupScheduleByUserId() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-user-info/{userId}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-user-info/{userId}", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ResponseDto editUserInfo(@PathVariable String userId, @RequestBody UserInfoDto userInfoDto) {
        return userService.editUserInfo(userId,userInfoDto);
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/search-super-admin")
    public @ResponseBody PagingDto searchSuperadmin(@RequestBody(required = false) String groupName, @RequestParam String category,
                                                    @RequestParam String q,
                                                    Pageable pageable){
        System.err.println(groupName);
        return userService.searchSuperadmin(groupName,category,q,pageable);
        
    }

}
