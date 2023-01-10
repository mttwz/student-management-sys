package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.AuthService;
import com.radnoti.studentmanagementsystem.service.UDService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final UDService udService;

    public AuthController(AuthService authService, UDService udService) {
        this.authService = authService;
        this.udService = udService;
    }

    @PostMapping(path = "/updatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void updateJwt(@RequestBody UserDTO userDTO) {
        authService.updateJwt(userDTO);


    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/login", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDTO.UserLoginDTO login(@RequestBody UserDTO userDTO){
        return authService.login(userDTO);
    }


    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/validatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map validateJwt(@RequestBody UserDTO userDTO) {
        return authService.validateJwt(userDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/test")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void test() {


    }
}
