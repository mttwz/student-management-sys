package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping(path = "/login", consumes = {"application/json"})
    public ResponseEntity<UserLoginDto> login(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(userDto));
    }

    @PostMapping(path = "/validate-jwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map validateJwt(@RequestBody UserDto userDto) {
        return authService.validateJwt(userDto);
    }

}
