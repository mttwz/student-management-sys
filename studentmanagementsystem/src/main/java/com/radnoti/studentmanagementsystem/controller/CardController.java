package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.service.CardService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    private final ResponseFactory responseFactory;


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    public ResponseEntity<Map> connectCardToStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",cardService.connectCardToStudent(studentDTO)));
    }


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> createCard(@RequestBody CardDTO cardDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.createCard(cardDTO)));
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/getcardbyuserid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByUserId(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.getCardByUserId(userDTO)));
    }
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/getcardbystudentid", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> getCardByStudentId(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.getCardByStudentId(studentDTO)));
    }


}
