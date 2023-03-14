/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

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

        Optional<Card> optionalCard = cardRepository.findById(savedCardId);
        if(optionalCard.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not exist");
        }

        if(!(Objects.equals(optionalCard.get().getHash(), cardDto.getHash()))){
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
        cardRepository.connectCardToStudent(studentDto.getId(), studentDto.getCardId());
        Optional<Student> optionalStudent = studentRepository.findById(studentDto.getId());
        Optional<Card> optionalCard = cardRepository.findById(studentDto.getCardId());

        if(optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        }
        if(optionalCard.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Card not found");
        }

        if(!Objects.equals(optionalStudent.get().getCardId().getId(), studentDto.getCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Card not assigned successfully");
        }

        return optionalStudent.get().getId();
    }

    @Transactional
    public Integer getCardByUserId(UserDto userDto){
        if(userDto.getId() == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User id is null");
        }
        Optional<User> optionalUser = userRepository.findById(userDto.getId());

        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User not exist");
        }

        Integer cardId = cardRepository.getCardByUserId(userDto.getId());
        return cardId;
    }


    @Transactional
    public Integer getCardByStudentId(StudentDto studentDto){
        if(studentDto.getId() == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Student id is null");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentDto.getId());

        if(optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Student not exist");
        }
        Integer cardId = cardRepository.getCardByStudentId(studentDto.getId());
        return cardId;
    }
}
