/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String jwt;
    private String searchText;

    private Integer WorkgroupId;
    private String workgroup;

    private String institution;




    public UserDTO(User user) {
        this.id = user.getId();
        this.roleName = user.getRoleId().getRoleType();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.birth = user.getBirth();
        this.email = user.getEmail();
        this.registeredAt = user.getRegisteredAt();
        this.isActivated = user.getIsActivated();
        this.activatedAt = user.getActivatedAt();
        this.isDeleted = user.getIsDeleted();
        this.deletedAt = user.getDeletedAt();
        this.activationCode = null;
    }

}
