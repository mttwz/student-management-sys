package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.AuthService;
import com.radnoti.studentmanagementsystem.security.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    private final UserDetailsServiceImp userDetailsServiceImp;


    @PostMapping(path = "/updatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void updateJwt(@RequestBody UserDTO userDTO) {
        authService.updateJwt(userDTO);
    }


    @PostMapping(path = "/login", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDTO.UserLoginDTO login(@RequestBody UserDTO userDTO){
        return authService.login(userDTO);
    }



    @PostMapping(path = "/validatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map validateJwt(@RequestBody UserDTO userDTO) {
        return authService.validateJwt(userDTO);
    }

}
