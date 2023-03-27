package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/password-reset")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping(path = "/{userEmail}")
    public void resetPassword(@PathVariable String userEmail) {
        passwordResetService.resetPassword(userEmail);
    }


}