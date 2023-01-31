/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author matevoros
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {

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
    private Collection<WorkgroupmembersDTO> workgroupmembersCollection;
    private StudentDTO student;
    private Collection<PasswordresetDTO> passwordresetCollection;
    private String jwt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginDTO implements Serializable {

        private Integer id;

        private String email;

        private String firstName;

        private String lastName;

        private String jwt;

    }
}
