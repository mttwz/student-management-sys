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
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;
import java.util.Optional;


/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class CardService {

    private final JwtConfig jwtConfig;

    private final CardRepository cardRepository;
    private final StudentRepository studentRepository;

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

        if(!(Objects.equals(optionalCard.get().getHash(), cardDTO.getHash()) && Objects.equals(optionalCard.get().getHash(), cardDTO.getHash()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not created");
        }
        return savedCardId;
    }


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
}
