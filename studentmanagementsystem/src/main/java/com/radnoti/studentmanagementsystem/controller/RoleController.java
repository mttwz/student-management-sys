package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.RoleService;
import com.radnoti.studentmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class RoleController {

    private final UserService userService;
    private final RoleService roleService;

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/set-user-role", consumes = {"application/json"}, produces = {"application/json"})
    public void setUserRole(@RequestBody UserDto userDto) {
        roleService.setUserRole(userDto);
    }
}
