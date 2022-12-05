/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;


/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping(path = "/updatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void updateJwt(@RequestBody UserDTO userDTO) {
        userService.updateJwt(userDTO);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/register", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void register(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.register(jwt, userDTO);
    }
    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/registerstudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void registerStudent(@RequestBody UserDTO userDTO){
        userService.registerStudent(userDTO);

    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/login", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDTO.UserLoginDTO login(@RequestBody UserDTO userDTO){
        return userService.login(userDTO);
    }


    @PostMapping(path = "/setuserIiactivated", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserIsActivated(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.setUserIsActivated(jwt, userDTO);

    }

    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteUser(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.deleteUser(jwt, userDTO);

    }

    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserRole(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.setUserRole(jwt, userDTO);
    }
    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/validatejwt", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map validateJwt(@RequestBody UserDTO userDTO) {
        return userService.validateJwt(userDTO);
    }



    @PostMapping(path = "/addstudenttoworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void addUserToWorkgroup(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.addUserToWorkgroup(jwt, userDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(@RequestBody UserDTO userDTO) {
        return userService.getWorkgroupScheduleByUserId(userDTO);
    }

}
