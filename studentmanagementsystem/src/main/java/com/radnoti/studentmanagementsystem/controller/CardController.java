package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.enums.Role;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
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


    @RolesAllowed({Role.Types.SUPERADMIN})
    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> connectCardToStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id",cardService.connectCardToStudent(studentDTO)));
    }


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map> connectCardToStudent(@RequestBody CardDTO cardDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(responseFactory.createResponse("id", cardService.createCard(cardDTO)));
    }


}
