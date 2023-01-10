/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.CardDTO;
import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author matevoros
 */
@Service
public class CardService {

    private final JwtUtil jwtUtil;

    private final CardRepository cardRepository;

    public CardService(JwtUtil jwtUtil, CardRepository cardRepository) {
        this.jwtUtil = jwtUtil;
        this.cardRepository = cardRepository;
    }


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

    @Transactional
    public void connectStudentToUser(StudentDTO studentDTO) {

            cardRepository.connectStudentToUser(studentDTO.getId(), studentDTO.getUserId());

    }


}
