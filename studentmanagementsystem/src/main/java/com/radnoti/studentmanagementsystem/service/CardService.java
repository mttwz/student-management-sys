/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
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
     * @param cardDTO The hash details in Json format. eg: {
     *     "hash":"veryOriginalHash"
     * }
     * @return The saved card id.
     * @throws ResponseStatusException on failed creation
     */

    @Transactional
    public Integer createCard(final CardDTO cardDTO) {
        Integer savedCardId = cardRepository.createCard(cardDTO.getHash());
        if(savedCardId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not created");
        }

        Optional<Card> optionalCard = cardRepository.findById(savedCardId);
        if(optionalCard.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not exist");
        }

        if(!(Objects.equals(optionalCard.get().getHash(), cardDTO.getHash()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not created");
        }
        return savedCardId;
    }

    /**
     * Connects an existing student with an existing card.
     * @param studentDTO The id of the existing student and the id of the existing card in Json format. eg:
     * {
     *     "id" : 1,
     *     "cardId" : 1
     * }
     * @return The id of the connected student
     * @throws ResponseStatusException on failed connection
     */
    @Transactional
    public Integer connectCardToStudent(StudentDTO studentDTO) {
        cardRepository.connectCardToStudent(studentDTO.getId(), studentDTO.getCardId());
        Optional<Student> optionalStudent = studentRepository.findById(studentDTO.getId());
        Optional<Card> optionalCard = cardRepository.findById(studentDTO.getCardId());

        if(optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        }
        if(optionalCard.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Card not found");
        }

        if(!Objects.equals(optionalStudent.get().getCardId().getId(), studentDTO.getCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Card not assigned successfully");
        }

        return optionalStudent.get().getId();
    }

    @Transactional
    public Integer getCardByUserId(UserDTO userDTO){
        if(userDTO.getId() == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User id is null");
        }
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());

        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User not exist");
        }

        Integer cardId = cardRepository.getCardByUserId(userDTO.getId());
        return cardId;
    }


    @Transactional
    public Integer getCardByStudentId(StudentDTO studentDTO){
        if(studentDTO.getId() == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Student id is null");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentDTO.getId());

        if(optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Student not exist");
        }
        Integer cardId = cardRepository.getCardByStudentId(studentDTO.getId());
        return cardId;
    }
}
