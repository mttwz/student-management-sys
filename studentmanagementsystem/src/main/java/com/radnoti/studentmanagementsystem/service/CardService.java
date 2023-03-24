/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.*;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.CardMapper;
import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;


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
     * Creates a new card in the database based on the provided DTO object.
     *
     * @param cardDto The DTO object representing the new card.
     * @return The ID of the newly created card.
     * @throws InvalidFormValueException if the provided DTO object is null or its hash value is null or empty.
     */
    @Transactional
    public ResponseDto createCard(CardDto cardDto) {
        if (cardDto == null || cardDto.getHash() == null || cardDto.getHash().trim().isEmpty()) {
            throw new InvalidFormValueException();
        }
        Card card = cardMapper.fromDtoToEntity(cardDto);
        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        card.setCreatedAt(currDate);
        card.setIsDeleted(false);
        cardRepository.save(card);
        return new ResponseDto(card.getId());
    }

    /**
     * This method sets the deleted flag of the card in the database based on the provided id.
     * The method checks the card existence and the deleted status.
     * If the card assigned to a student then removes the connection
     *
     * @param cardIdString a string representing the ID of the card
     * @throws InvalidIdException  if the provided card's id is invalid eg: if the id contains a string
     * @throws CardNotExistException if the provided DTO object is null or its hash value is null or empty.
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
     * Connects a card to a student in the database based on the provided DTO object.
     *
     * @param studentDto The DTO object containing the student's id and the card's id.
     * @throws InvalidFormValueException if the provided ID or card ID value is null.
     * @throws StudentNotExistException if the student with the provided ID does not exist in the database.
     * @throws CardNotExistException if the card with the provided card ID does not exist in the database.
     */
    @Transactional
    public void assignCardToStudent(StudentDto studentDto) {
        if (studentDto.getId() == null || studentDto.getCardId() == null) {
            throw new InvalidFormValueException();
        }

        Student student = studentRepository.findById(studentDto.getId())
                .orElseThrow(StudentNotExistException::new);

        Card card = cardRepository.findById(studentDto.getCardId())
                .orElseThrow(CardNotExistException::new);

        if (card.getIsAssigned()){
            throw new CardAlreadyAssignedException();
        }

        if (card.getIsDeleted()){
            throw new CardAlreadyDeletedException();
        }

        if(student.getCardId() != null){
            throw new AnotherCardAlreadyAssignedException();
        }

        student.setCardId(card);
        card.setIsAssigned(true);
        card.setLastAssignedTo(student.getId());
    }

    /**
     * Retrieves the ID of the card assigned to the user with the provided ID.
     *
     * @param userIdString User id as String.
     * @return The ID of the card assigned to the user.
     * @throws NullFormValueException if the provided ID value is null.
     * @throws UserNotExistException if the user with the provided ID does not exist in the database.
     * @throws CardNotAssignedException if the user with the provided ID does not have a card assigned to them.
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
     * Retrieves the ID of the card assigned to the student with the provided ID.
     *
     * @param studentIdString User id as String.
     * @return The ID of the card assigned to the student.
     * @throws NullFormValueException if the provided ID value is null.
     * @throws StudentNotExistException if the student with the provided ID does not exist in the database.
     * @throws CardNotAssignedException if the student with the provided ID does not have a card assigned to them.
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


}
