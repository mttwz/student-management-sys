/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author matevoros
 */
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Procedure
    Integer createCard(String hash);
    @Procedure
    void connectCardToStudent(Integer studentId, Integer cardId);

    @Procedure
    Integer getCardByStudentId(Integer studentId);

    @Procedure
    Integer getCardByUserId(Integer userId);
}
