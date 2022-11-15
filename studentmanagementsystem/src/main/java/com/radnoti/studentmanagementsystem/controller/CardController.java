/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.dto.CardDTO;
import com.radnoti.studentmanagementsystem.service.CardService;
import com.radnoti.studentmanagementsystem.util.ResponseFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author matevoros
 */
@RestController
@RequestMapping(path = "/card")
public class CardController {


    @Autowired
    private CardService cardService;

    ResponseFactory rf = new ResponseFactory();

    @PostMapping(path = "/createcard", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void connectCardToStudent(@RequestHeader("token") String jwt, @RequestBody CardDTO cardDTO) {
        cardService.createCard(jwt, cardDTO);


    }

}
