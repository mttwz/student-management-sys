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

    /**
     * Generates a password reset code for the given user email.
     *
     * @param userEmail the email of the user to generate the reset code for.
     */
    @PostMapping(path = "/{userEmail}")
    public void generateResetCode(@PathVariable String userEmail) {
        passwordResetService.generatePasswordResetCode(userEmail);
    }


    /**
     * Resets the password of a user.
     *
     * @param userDto the user DTO containing the new password and reset code.
     * @throws NoSuchAlgorithmException if an error occurs while resetting the password.
     */
    @PostMapping(path = "/reset-password")
    public void resetPassword(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {

        passwordResetService.resetPassword(userDto);

    }


}
