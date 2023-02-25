/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class CardService {

    private final JwtConfig jwtConfig;

    private final CardRepository cardRepository;

    @Transactional
    public void createCard(CardDTO cardDTO) {

            cardRepository.createCard(cardDTO.getHash());


    }


    @Transactional
    public void connectCardToStudent(StudentDTO studentDTO) {

/*            Optional<Student> student = studentRepository.findById(studentDTO.getId());
            if (student.isPresent()){
                student.get().setCardId(new Card(studentDTO.getCardId()));
                studentRepository.save(student.get());
            }*/
            cardRepository.connectCardToStudent(studentDTO.getId(), studentDTO.getCardId());

    }


    public CardDTO getUserCard(UserDTO userDTO) {
        return new CardDTO(cardRepository.getUserCard(userDTO.getId()));
    }
}
