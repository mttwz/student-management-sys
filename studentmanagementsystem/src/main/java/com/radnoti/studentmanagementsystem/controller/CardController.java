package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.PagingDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/assign-card-to-student")
    public void connectCardToStudent(@RequestBody StudentDto studentDto) {
        cardService.assignCardToStudent(studentDto);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/unassign-card-from-student/{studentId}")
    public void disconnectCardFromStudent(@PathVariable String studentId) {
        cardService.unassignCardfromStudent(studentId);
    }



    @PostMapping(path = "/create-card", consumes = {"text/plain"}, produces = {"application/json"})
    public ResponseDto createCard(@RequestBody String cardHash) {
        return cardService.createCard(cardHash);

    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-card/{cardId}")
    public void deleteCard(@PathVariable String cardId) {
        cardService.deleteCard(cardId);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/restore-deleted-card/{cardId}")
    public void restoreDeletedCard(@PathVariable String cardId) {
        cardService.restoreDeletedCard(cardId);
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
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-card")
    public PagingDto getAllCard(Pageable pageable) {
        return cardService.getAllCard(pageable);
    }

    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-all-avaliable-card")
    public List<CardDto> getAllAvaliableCard() {
        return cardService.getAllAvaliableCard();
    }


}
