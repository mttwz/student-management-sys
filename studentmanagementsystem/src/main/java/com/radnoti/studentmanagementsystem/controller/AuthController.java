package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;


@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping(path = "/login", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<UserLoginDto> login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(authService.login(userDto));
    }

    @PostMapping(path = "/validate-jwt", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Boolean> validateJwt(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.validateJwt(userDto));
    }

}
