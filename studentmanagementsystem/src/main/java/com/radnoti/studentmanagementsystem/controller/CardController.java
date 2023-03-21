package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.service.CardService;
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


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/connect-card-to-student", consumes = {"application/json"}, produces = {"application/json"})
    public void connectCardToStudent(@RequestBody StudentDto studentDto) {
        cardService.connectCardToStudent(studentDto);
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/create-card", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Map> createCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", cardService.createCard(cardDto)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/delete-card", consumes = {"application/json"}, produces = {"application/json"})
    public void deleteCard(@RequestBody CardDto cardDto) {
        cardService.deleteCard(cardDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-card-by-user-id", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByUserId(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", cardService.getCardByUserId(userDto)));
    }
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/get-card-by-student-id", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByStudentId(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("id", cardService.getCardByStudentId(studentDto)));
    }


}
