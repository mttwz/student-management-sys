/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String hash;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.hash = card.getHash();
    }

    public CardDTO(Integer id) {
        this.id = id;
    }
}
