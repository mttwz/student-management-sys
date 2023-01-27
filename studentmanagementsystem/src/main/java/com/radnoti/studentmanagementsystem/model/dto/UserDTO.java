/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String firstName;

    private String lastName;

    private String phone;

    private Date birth;

    private String email;

    private String password;

    private Integer workgroupId;

    private String jwt;

    private Date jwtExpireDate;

    private Date registeredAt;

    private String activationCode;

    private boolean isActivated;

    private Date activatedAt;

    private boolean isDeleted;

    private Date deletedAt;

    private String roleName;

    private Collection<PasswordresetDTO> passwordresetCollection;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserLoginDTO implements Serializable {

        private Integer id;

        private String email;

        private String firstName;

        private String lastName;

        private String jwt;

    }
}
