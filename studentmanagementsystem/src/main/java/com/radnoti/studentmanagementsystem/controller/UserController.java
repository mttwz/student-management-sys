package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/register-user")
    public ResponseEntity<Map> adduser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(Map.of("id", userService.adduser(userDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-is-activated", consumes = {"application/json"}, produces = {"application/json"})
    public void setUserIsActivated(@RequestBody UserDto userDto) {
        userService.setUserIsActivated(userDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/delete-user", consumes = {"application/json"}, produces = {"application/json"})
    public void deleteUser(@RequestBody UserDto userDto) {
        userService.deleteUser(userDto);
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-all-user", consumes = {"application/json"}, produces = {"application/json"})
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
    @PostMapping(path = "/search-super-admin")
    public @ResponseBody List<UserInfoDto> searchSuperadmin(@RequestParam String filter,
                                                            @RequestParam String q,
                                                            Pageable pageable){

        //http://127.0.0.1:8080/api/v1/user/search-super-admin?filter=all-users&q=&page=0&size=3&sort=id,asc
        return userService.searchSuperadmin(filter,q,pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-user-from-workgroup", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ArrayList<UserDto> getAllUserIdFromWorkgroup(@RequestBody UserDto userDto){
        return userService.getUserFromWorkgroup(userDto);
    }


}
