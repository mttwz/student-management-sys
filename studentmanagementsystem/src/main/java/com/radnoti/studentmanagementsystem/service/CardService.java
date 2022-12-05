/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.Card;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author matevoros
 */
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void createCard(String jwt, CardDTO cardDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            cardRepository.createCard(cardDTO.getHash());


        }
    }
}
