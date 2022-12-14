package com.radnoti.studentmanagementsystem.controller;


import com.radnoti.studentmanagementsystem.dto.*;
import com.radnoti.studentmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/superadmin")
public class SuperadminController {


    private final UserService userService;
    private final StudentService studentService;
    private final  SuperadminService superadminService;
    private final CardService cardService;
    private final WorkgroupService workgroupService;

    public SuperadminController(UserService userService, StudentService studentService, SuperadminService superadminService, CardService cardService, WorkgroupService workgroupService) {
        this.userService = userService;
        this.studentService = studentService;
        this.superadminService = superadminService;
        this.cardService = cardService;
        this.workgroupService = workgroupService;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/register", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void register(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        userService.register(jwt, userDTO);
    }

    @PostMapping(path = "/setuserIiactivated", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserIsActivated(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        superadminService.setUserIsActivated(jwt, userDTO);

    }

    @PostMapping(path = "/deleteuser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteUser(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        superadminService.deleteUser(jwt, userDTO);

    }

    @PostMapping(path = "/setuserrole", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void setUserRole(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        superadminService.setUserRole(jwt, userDTO);

    }

    @PostMapping(path = "/addstudenttoworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void addUserToWorkgroup(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO) {
        superadminService.addUserToWorkgroup(jwt, userDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(path = "/getworkgroupschedulebyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(@RequestBody UserDTO userDTO) {
        return userService.getWorkgroupScheduleByUserId(userDTO);
    }

    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestHeader("Authorization") String jwt, @RequestBody StudentDTO studentDTO) {
        superadminService.connectCardToStudent(jwt, studentDTO);
    }

    @PostMapping(path = "/connectstudenttoUser", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectStudentToUser(@RequestHeader("Authorization") String jwt, @RequestBody StudentDTO studentDTO) {
        superadminService.connectStudentToUser(jwt, studentDTO);
    }

    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestHeader("Authorization") String jwt, @RequestBody CardDTO cardDTO) {
        superadminService.createCard(jwt, cardDTO);
    }

    @PostMapping(path = "/createworkgroup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void createWorkgroup(@RequestHeader("Authorization") String jwt, @RequestBody WorkgroupDTO workgroupDTO) {
        workgroupService.createWorkgroup(jwt, workgroupDTO);

    }

}
