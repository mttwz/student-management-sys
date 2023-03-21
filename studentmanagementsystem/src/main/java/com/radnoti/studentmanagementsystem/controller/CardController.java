package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
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
    public ResponseDto createCard(@RequestBody CardDto cardDto) {
        return cardService.createCard(cardDto);

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-card/{cardId}")
    public void deleteCard(@PathVariable String cardId) {
        cardService.deleteCard(cardId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-card-by-user-id/{userId}")
    public ResponseDto getCardByUserId(@PathVariable String userId) {
        return cardService.getCardByUserId(userId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-card-by-student-id/{studentId}")
    public ResponseDto getCardByStudentId(@PathVariable String studentId) {
        return cardService.getCardByStudentId(studentId);
    }


}
