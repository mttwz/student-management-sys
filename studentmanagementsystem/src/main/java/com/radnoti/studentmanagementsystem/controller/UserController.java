/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping(path = "/updatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public @ResponseBody void updateJwt(@RequestBody UserDTO userDTO) {
        userService.updateJwt(userDTO);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/register", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void register(@RequestHeader("token") String jwt, @RequestBody UserDTO userDTO) {
        userService.register(jwt, userDTO);
    }
    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/registerstudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void registerStudent(@RequestBody UserDTO userDTO){
        System.out.println(userDTO.getEmail());
        System.out.println(userDTO.getPassword());

        userService.registerStudent(userDTO);

    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/login", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserLoginDTO login(@RequestBody UserDTO userDTO){
        return userService.login(userDTO);
    }

    @PostMapping(path = "/setuserIiactivated", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserIsActivated(@RequestHeader("token") String jwt, @RequestBody UserDTO userDTO) {
        userService.setUserIsActivated(jwt, userDTO);

    }

    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteUser(@RequestHeader("token") String jwt, @RequestBody UserDTO userDTO) {
        userService.deleteUser(jwt, userDTO);

    }

    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserRole(@RequestHeader("token") String jwt, @RequestBody UserDTO userDTO) {
        userService.setUserRole(jwt, userDTO);
    }
    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/validatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody boolean validateJwt(@RequestBody UserDTO userDTO) {
        return userService.validateJwt(userDTO);
    }

}
