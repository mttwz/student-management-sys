package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.service.AuthService;
import com.radnoti.studentmanagementsystem.security.UserDetailsServiceImp;
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

//    private final UserDetailsServiceImp userDetailsServiceImp;


//    @PostMapping(path = "/updatejwt", consumes = {"application/json"})
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody void updateJwt(@RequestBody UserDTO userDTO) {
//        authService.updateJwt(userDTO);
//    }


    @PostMapping(path = "/login", consumes = {"application/json"})
    public ResponseEntity<UserLoginDTO> login(@RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(userDTO));
    }

    @PostMapping(path = "/validatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map validateJwt(@RequestBody UserDTO userDTO) {
        return authService.validateJwt(userDTO);
    }

}
