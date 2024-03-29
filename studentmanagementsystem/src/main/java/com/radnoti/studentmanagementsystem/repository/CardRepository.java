/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author matevoros
 */
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Query("select s.cardId from Student s " +
            "where s.userId.id = :userId")
    Optional<Card> getCardByUserId(Integer userId);

    @Query("select c from Card c " +
            "where c.hash = :hash")
    Optional<Card> findByHash(String hash);


    @Query("select c from Card c " +
            "join User u on u.id = c.student.userId.id")
    Page<Card> findAll(Pageable pageable);

    @Query("select c from Card c " +
            "where c.isDeleted = false and " +
            "c.isAssigned = false " +
            "order by c.createdAt desc")
    List<Card> findAllAvaliableCard();


    @Query("select c from Card c where c.lastAssignedTo = :studentId")
    Optional<Card> getCardByLastAssignedTo(Integer studentId);
}
