package com.radnoti.studentmanagementsystem.controller;


import com.radnoti.studentmanagementsystem.dto.*;
import com.radnoti.studentmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/superadmin")
public class SuperadminController {


    private final UserService userService;
    private final StudentService studentService;

    private final CardService cardService;
    private final WorkgroupService workgroupService;

    public SuperadminController(UserService userService, StudentService studentService, CardService cardService, WorkgroupService workgroupService) {
        this.userService = userService;
        this.studentService = studentService;
        this.cardService = cardService;
        this.workgroupService = workgroupService;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/adduser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void adduser(@RequestBody UserDTO userDTO) {
        userService.adduser(userDTO);
    }

    @PostMapping(path = "/setuserisactivated", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserIsActivated(@RequestBody UserDTO userDTO) {
        userService.setUserIsActivated(userDTO);

    }

    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteUser(@RequestBody UserDTO userDTO) {
        userService.deleteUser(userDTO);

    }

    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserRole(@RequestBody UserDTO userDTO) {
        userService.setUserRole(userDTO);

    }

    @PostMapping(path = "/addstudenttoworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void addUserToWorkgroup(@RequestBody UserDTO userDTO) {
        userService.addUserToWorkgroup(userDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(@RequestBody UserDTO userDTO) {
        return userService.getWorkgroupScheduleByUserId(userDTO);
    }

    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestBody StudentDTO studentDTO) {
        cardService.connectCardToStudent(studentDTO);
    }

    @PostMapping(path = "/connectstudenttoUser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectStudentToUser(@RequestBody StudentDTO studentDTO) {
        cardService.connectStudentToUser(studentDTO);
    }

    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestBody CardDTO cardDTO) {
        cardService.createCard(cardDTO);
    }

    @PostMapping(path = "/createworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroup(@RequestBody WorkgroupDTO workgroupDTO) {
        workgroupService.createWorkgroup(workgroupDTO);

    }

}
