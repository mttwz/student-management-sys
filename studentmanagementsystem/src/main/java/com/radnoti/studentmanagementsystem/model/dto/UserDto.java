/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author matevoros
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {

    private Integer id;
    private String roleName;
    private String firstName;
    private String lastName;
    private String phone;
    private Date birth;
    private String email;
    private String password;
    private Date registeredAt;
    private String activationCode;
    private Boolean isActivated;
    private Date activatedAt;
    private Boolean isDeleted;
    private Date deletedAt;
    private String jwt;

}
