package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/password-reset")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping(path = "/{userEmail}")
    public void generateResetCode(@PathVariable String userEmail) {
        passwordResetService.generatePasswordResetCode(userEmail);
    }


    @PostMapping(path = "/reset-password")
    public void resetPassword(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {

        passwordResetService.resetPassword(userDto);

    }


}
