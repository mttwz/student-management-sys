/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
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


    /**
     *  Creates a card with a custom hash.
     * @param cardDto The hash details in Json format. eg: {
     *     "hash":"veryOriginalHash"
     * }
     * @return The saved card id.
     * @throws ResponseStatusException on failed creation
     */

    @Transactional
    public Integer createCard(final CardDto cardDto) {
        Integer savedCardId = cardRepository.createCard(cardDto.getHash());
        if(savedCardId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not created");
        }
        return savedCardId;
    }

    /**
     * Connects an existing student with an existing card.
     * @param studentDto The id of the existing student and the id of the existing card in Json format. eg:
     * {
     *     "id" : 1,
     *     "cardId" : 1
     * }
     * @return The id of the connected student
     * @throws ResponseStatusException on failed connection
     */
    @Transactional
    public Integer connectCardToStudent(StudentDto studentDto) {
        if (studentDto.getId()==null || studentDto.getCardId() == null){
            throw new NullFormValueException();
        }
        cardRepository.connectCardToStudent(studentDto.getId(), studentDto.getCardId());

        Student student = studentRepository.findById(studentDto.getId()).orElseThrow(StudentNotExistException::new);

        cardRepository.findById(studentDto.getCardId()).orElseThrow(CardNotExistException::new);

        if(!Objects.equals(student.getCardId().getId(), studentDto.getCardId())){
            throw new CardNotAssignedException();
        }

        return student.getId();
    }

    @Transactional
    public Integer getCardByUserId(UserDto userDto){
        if(userDto.getId() == null){
            throw new NullFormValueException();
        }
        userRepository.findById(userDto.getId()).orElseThrow(UserNotExistException::new);

        Integer cardId = cardRepository.getCardByUserId(userDto.getId());
        if (cardId == null){
            throw new CardNotExistException();
        }
        return cardId;
    }


    @Transactional
    public Integer getCardByStudentId(StudentDto studentDto){
        if(studentDto.getId() == null){
            throw new NullFormValueException();
        }
        studentRepository.findById(studentDto.getId()).orElseThrow(StudentNotExistException::new);

        Integer cardId = cardRepository.getCardByStudentId(studentDto.getId());
        if (cardId == null){
            throw new CardNotExistException();
        }
        return cardId;
    }
}
