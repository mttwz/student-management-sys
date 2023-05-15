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
@CrossOrigin(origins = "${cross-origin}")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    /**
     * Assigns a card to a student.
     *
     * @param studentDto The student information containing the card details.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/assign-card-to-student")
    public void connectCardToStudent(@RequestBody StudentDto studentDto) {
        cardService.assignCardToStudent(studentDto);
    }

    /**
     * Unassigns a card from a student.
     *
     * @param studentId The ID of the student to unassign the card from.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/unassign-card-from-student/{studentId}")
    public void disconnectCardFromStudent(@PathVariable String studentId) {
        cardService.unassignCardfromStudent(studentId);
    }



    /**
     * Creates a new card with the provided card hash.
     *
     * @param cardHash The card hash value.
     * @return A {@link ResponseDto} containing the ID of the created card.
     */
    @PostMapping(path = "/create-card", consumes = {"text/plain"}, produces = {"application/json"})
    public ResponseDto createCard(@RequestBody String cardHash) {
        return cardService.createCard(cardHash);
    }


    /**
     * Deletes a card with the specified card ID.
     *
     * @param cardId The ID of the card to delete.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-card/{cardId}")
    public void deleteCard(@PathVariable String cardId) {
        cardService.deleteCard(cardId);
    }


    /**
     * Restores a previously deleted card with the specified card ID.
     *
     * @param cardId The ID of the card to restore.
     */

    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/restore-deleted-card/{cardId}")
    public void restoreDeletedCard(@PathVariable String cardId) {
        cardService.restoreDeletedCard(cardId);
    }


    /**
     * Retrieves a card associated with the specified user ID.
     *
     * @param userId The ID of the user.
     * @return A {@link ResponseDto} containing the card information.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-card-by-user-id/{userId}")
    public ResponseDto getCardByUserId(@PathVariable String userId) {
        return cardService.getCardByUserId(userId);
    }


    /**
     * Retrieves a card associated with the specified student ID.
     *
     * @param studentId The ID of the student.
     * @return A {@link ResponseDto} containing the card information.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-card-by-student-id/{studentId}")
    public ResponseDto getCardByStudentId(@PathVariable String studentId) {
        return cardService.getCardByStudentId(studentId);
    }

    /**
     * Retrieves all cards with paging support.
     *
     * @param pageable The paging information.
     * @return  containing the list of cards and paging details.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-card")
    public PagingDto getAllCard(Pageable pageable) {
        return cardService.getAllCard(pageable);
    }

    /**
     * Retrieves all available cards.
     *
     * @return A list of  representing the available cards.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-all-avaliable-card")
    public List<CardDto> getAllAvaliableCard() {
        return cardService.getAllAvaliableCard();
    }


}
