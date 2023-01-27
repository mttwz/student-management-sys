package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/connectcardtostudent", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestBody StudentDTO studentDTO) {
        cardService.connectCardToStudent(studentDTO);
    }


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestBody CardDTO cardDTO) {
        cardService.createCard(cardDTO);
    }
}
