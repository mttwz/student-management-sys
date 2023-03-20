/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.CardMapper;
import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;
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

    /**
     * Creates a new card in the database based on the provided DTO object.
     *
     * @param cardDto The DTO object representing the new card.
     * @return The ID of the newly created card.
     * @throws InvalidFormValueException if the provided DTO object is null or its hash value is null or empty.
     */
    @Transactional
    public Integer createCard(final CardDto cardDto) {
        if (cardDto == null || cardDto.getHash() == null || cardDto.getHash().trim().isEmpty()) {
            throw new InvalidFormValueException();
        }
        Card card = cardMapper.fromDtoToEntity(cardDto);
        Date currDate = Date.from(java.time.ZonedDateTime.now().toInstant());
        card.setCreatedAt(currDate);
        cardRepository.save(card);
        return card.getId();
    }

    @Transactional
    public void deleteCard(final CardDto cardDto) {
        if (cardDto == null || cardDto.getHash() == null || cardDto.getHash().trim().isEmpty()) {
            throw new InvalidFormValueException();
        }
        Card card = cardRepository.findById(cardDto.getId()).orElseThrow(CardNotExistException::new);

        Date currDate = Date.from(java.time.ZonedDateTime.now().toInstant());
        card.setDeleted(true);
        card.setDeletedAt(currDate);
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
    public void connectCardToStudent(StudentDto studentDto) {
        if (studentDto.getId() == null || studentDto.getCardId() == null) {
            throw new InvalidFormValueException();
        }

        Student student = studentRepository.findById(studentDto.getId())
                .orElseThrow(StudentNotExistException::new);

        Card card = cardRepository.findById(studentDto.getCardId())
                .orElseThrow(CardNotExistException::new);

        student.setCardId(card);
    }

    /**
     * Retrieves the ID of the card assigned to the user with the provided ID.
     *
     * @param userDto The DTO object containing the user's id.
     * @return The ID of the card assigned to the user.
     * @throws NullFormValueException if the provided ID value is null.
     * @throws UserNotExistException if the user with the provided ID does not exist in the database.
     * @throws CardNotAssignedException if the user with the provided ID does not have a card assigned to them.
     */
    @Transactional
    public Integer getCardByUserId(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new NullFormValueException();
        }
        userRepository.findById(userDto.getId()).orElseThrow(UserNotExistException::new);

        Integer cardId = cardRepository.getCardByUserId(userDto.getId());
        if (cardId == null) {
            throw new CardNotAssignedException();
        }
        return cardId;
    }

    /**
     * Retrieves the ID of the card assigned to the student with the provided ID.
     *
     * @param studentDto The DTO object containing the student's id.
     * @return The ID of the card assigned to the student.
     * @throws NullFormValueException if the provided ID value is null.
     * @throws StudentNotExistException if the student with the provided ID does not exist in the database.
     * @throws CardNotAssignedException if the student with the provided ID does not have a card assigned to them.
     */
    @Transactional
    public Integer getCardByStudentId(StudentDto studentDto) {
        if (studentDto.getId() == null) {
            throw new NullFormValueException();
        }
        Student student = studentRepository.findById(studentDto.getId())
                .orElseThrow(StudentNotExistException::new);
        Integer cardId = student.getCardId().getId();

        if (cardId == null) {
            throw new CardNotAssignedException();
        }
        return cardId;
    }


}
