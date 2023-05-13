/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.*;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.FormValueNullException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.CardMapper;
import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.PagingDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    private final IdValidatorUtil idValidatorUtil;

    /**
     * Creates a new card in the database based on the provided card hash.
     *
     * @param cardHash The hash value of the new card.
     * @return A ResponseDto object containing the ID of the newly created card.
     * @throws FormValueInvalidException if the provided card hash is null or empty.
     * @throws CardAlreadyAssignedException if a card with the provided hash already exists.
     */
    @Transactional
    public ResponseDto createCard(String cardHash) {
        if (cardHash == null || cardHash.isEmpty()) {
            throw new FormValueInvalidException();
        }
        Optional<Card> optionalCard = cardRepository.findByHash(cardHash);
        if(optionalCard.isPresent()){
            throw new CardAlreadyAssignedException();//TODO: card already exist exception
        }

        Card card = new Card();
        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        card.setHash(cardHash);
        card.setCreatedAt(currDate);
        card.setIsDeleted(false);
        card.setIsAssigned(false);
        cardRepository.save(card);
        return new ResponseDto(card.getId());
    }

    /**
     * Deletes a card from the database based on the provided card ID.
     *
     * @param cardIdString The ID of the card as a string.
     * @throws IllegalArgumentException if the provided card ID is invalid.
     * @throws CardNotExistException if the card with the provided ID does not exist.
     * @throws CardAlreadyDeletedException if the card has already been marked as deleted.
     */
    @Transactional
    public void deleteCard(String cardIdString) {
        Integer cardId = idValidatorUtil.idValidator(cardIdString);

        Card card = cardRepository.findById(cardId).orElseThrow(CardNotExistException::new);

        if(card.getIsDeleted()){
            throw new CardAlreadyDeletedException();
        }

        studentRepository.findById(card.getLastAssignedTo())
                .ifPresent(s -> s.setCardId(null));


        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        card.setIsDeleted(true);
        card.setDeletedAt(currDate);
        card.setIsAssigned(false);
        cardRepository.save(card);
    }


    /**
     * Restores a deleted card in the database based on the provided card ID.
     * If the card is assigned to a student who currently does not have a card assigned,
     * the method re-establishes the connection between the student and the card.
     *
     * @param cardIdString The ID of the card as a string.
     * @throws InvalidIdException If the provided card ID is invalid.
     * @throws CardNotExistException If the card with the provided ID does not exist.
     * @throws CardNotDeletedException If the card is not marked as deleted and cannot be restored.
     */
    @Transactional
    public void restoreDeletedCard(String cardIdString) {
        Integer cardId = idValidatorUtil.idValidator(cardIdString);

        Card card = cardRepository.findById(cardId).orElseThrow(CardNotExistException::new);

        Optional<Student> student = studentRepository.findById(card.getLastAssignedTo());

        if (student.isPresent() && student.get().getCardId() == null){
            student.get().setCardId(card);
            card.setIsAssigned(true);
        }

        if(!card.getIsDeleted()){
            throw new CardNotDeletedException();
        }

        card.setIsDeleted(false);
        cardRepository.save(card);
    }


    /**
     * Assigns a card to a student in the database based on the provided StudentDto object.
     *
     * @param studentDto The DTO object representing the student and the card to be assigned.
     * @throws FormValueInvalidException If the provided StudentDto object is null or if the student's ID or card ID is null.
     * @throws StudentNotExistException If the student with the provided ID does not exist.
     * @throws CardNotExistException If the card with the provided ID does not exist.
     * @throws CardAlreadyAssignedException If the card is already assigned to another student.
     * @throws CardAlreadyDeletedException If the card is marked as deleted and cannot be assigned.
     * @throws AnotherCardAlreadyAssignedException If the student already has another card assigned.
     */
    @Transactional
    public void assignCardToStudent(StudentDto studentDto) {
        if (studentDto.getUserId() == null || studentDto.getCardId() == null) {
            throw new FormValueInvalidException();
        }

        User user = userRepository.findById(studentDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        Card card = cardRepository.findById(studentDto.getCardId())
                .orElseThrow(CardNotExistException::new);

        if (card.getIsAssigned()){
            throw new CardAlreadyAssignedException();
        }

        if (card.getIsDeleted()){
            throw new CardAlreadyDeletedException();
        }

        if(user.getStudent().getCardId() != null){
            throw new AnotherCardAlreadyAssignedException();
        }

        user.getStudent().setCardId(card);
        card.setIsAssigned(true);
        card.setLastAssignedTo(user.getStudent().getId());
    }

    /**
     * Retrieves the card associated with the provided user ID from the database.
     *
     * @param userIdString A string representing the ID of the user.
     * @return A ResponseDto object containing the ID of the retrieved card.
     * @throws InvalidIdException If the provided user ID is invalid.
     * @throws UserNotExistException If the user with the provided ID does not exist.
     * @throws CardNotExistException If the card associated with the user ID does not exist.
     * @throws CardAlreadyDeletedException If the retrieved card is marked as deleted.
     */
    @Transactional
    public ResponseDto getCardByUserId(String userIdString) {
        Integer userId = idValidatorUtil.idValidator(userIdString);

        userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        Card card = cardRepository.getCardByUserId(userId)
                .orElseThrow(CardNotExistException::new);

        if (Boolean.TRUE.equals(card.getIsDeleted())) {
            throw new CardAlreadyDeletedException();
        }
        return new ResponseDto(card.getId());
    }

    /**
     * Retrieves the card associated with the provided student ID from the database.
     *
     * @param studentIdString A string representing the ID of the student.
     * @return A ResponseDto object containing the ID of the retrieved card.
     * @throws InvalidIdException If the provided student ID is invalid.
     * @throws StudentNotExistException If the student with the provided ID does not exist.
     * @throws CardNotAssignedException If the student does not have an assigned card.
     */
    @Transactional
    public ResponseDto getCardByStudentId(String studentIdString) {
        Integer studentId = idValidatorUtil.idValidator(studentIdString);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotExistException::new);

        if (student.getCardId() == null) {
            throw new CardNotAssignedException();
        }

        return new ResponseDto(student.getCardId().getId());
    }



    /**
     * Retrieves all cards from the database with pagination.
     *
     * @param pageable The pagination information.
     * @return A PagingDto object containing the list of card DTOs and pagination details.
     */
    @Transactional
    public PagingDto getAllCard(Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(cardPage.getTotalPages());
        pagingDto.setCardDtoList(cardPage.stream().map(cardMapper::fromEntityToDto).toList());

        return pagingDto;
    }



    /**
     * Unassigns the card from the student with the provided student ID.
     *
     * @param studentIdString A string representing the ID of the student.
     * @throws InvalidIdException If the provided student ID is invalid.
     * @throws StudentNotExistException If the student with the provided ID does not exist.
     * @throws CardNotAssignedException If the student does not have an assigned card.
     */
    @Transactional
    public void unassignCardfromStudent(String studentIdString) {
        Integer studentId = idValidatorUtil.idValidator(studentIdString);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotExistException::new);

        if (student.getCardId() == null){
            throw new CardNotAssignedException();
        }

        student.setCardId(null);
        studentRepository.save(student);
    }


    /**
     * Retrieves all available cards from the database.
     *
     * @return A list of CardDto objects representing the available cards.
     */
    @Transactional
    public List<CardDto> getAllAvaliableCard() {
        return cardRepository.findAllAvaliableCard().stream().map(cardMapper::fromEntityToMinimalDto).collect(Collectors.toList());
    }
}
