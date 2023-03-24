/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 *
 * @author matevoros
 */
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Query("select s.cardId from Student s " +
            "where s.userId.id = :userId")
    Optional<Card> getCardByUserId(Integer userId);
}
