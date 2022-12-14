/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.dto;

import com.radnoti.studentmanagementsystem.model.Card;

import java.io.Serializable;

/**
 *
 * @author matevoros
 */

public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String hash;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.hash = card.getHash();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
