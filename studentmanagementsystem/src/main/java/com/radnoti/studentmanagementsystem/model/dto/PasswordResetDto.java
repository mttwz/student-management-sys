/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetDto {

    private Integer id;

    private String resetCode;

    private ZonedDateTime expireDate;

    private boolean isUsed;

    private UserDto userDto;

}
