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
    public Integer createCard(CardDTO cardDTO) {
        Integer cardId = cardRepository.createCard(cardDTO.getHash());
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent() && Objects.equals(optionalCard.get().getHash(), cardDTO.getHash())){
            return cardId;
        }else throw new ResponseStatusException(HttpStatus.CONFLICT, "Card not created");
    }


    @Transactional
    public Integer connectCardToStudent(StudentDTO studentDTO) {
        /*            Optional<Student> student = studentRepository.findById(studentDTO.getId());
            if (student.isPresent()){
                student.get().setCardId(new Card(studentDTO.getCardId()));
                studentRepository.save(student.get());
            }*/
        try {
            cardRepository.connectCardToStudent(studentDTO.getId(), studentDTO.getCardId());
            Optional<Student> optionalStudent = studentRepository.findById(studentDTO.getId());
            if (optionalStudent.isPresent() && Objects.equals(optionalStudent.get().getCardId().getId(), studentDTO.getCardId())){
                return optionalStudent.get().getId();
            }else throw new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Card can not be assigned to the user");
        }




    }


//    public CardDTO getUserCard(UserDTO userDTO) {
//        return new CardDTO(cardRepository.getUserCard(userDTO.getId()));
//    }
}
