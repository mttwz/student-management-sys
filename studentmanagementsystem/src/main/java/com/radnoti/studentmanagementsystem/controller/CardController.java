package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.CardService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    private final ResponseFactory responseFactory;


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/connect-card-to-student", consumes = {"application/json"})
    public ResponseEntity<Map> connectCardToStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",cardService.connectCardToStudent(studentDto)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/create-card", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> createCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.createCard(cardDto)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-card-by-user-id", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByUserId(@RequestBody UserDto userDto) {
        System.err.println("asd");
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.getCardByUserId(userDto)));
    }
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-card-by-student-id", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByStudentId(@RequestBody StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.getCardByStudentId(studentDto)));
    }


}
