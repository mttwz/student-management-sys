/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
public class CardDto {

    private Integer id;

    private String hash;

    private ZonedDateTime createdAt;

    private Boolean isDeleted;

    private ZonedDateTime deletedAt;

    private Boolean isAssigned;

    private Integer lastAssignedTo;


}
