package com.radnoti.studentmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {

    private Integer id;
    private String roleName;
    private String firstName;
    private String lastName;
    private String phone;
    private Date birth;
    private String email;
    private Date registeredAt;
    private Boolean isActivated;
    private Date activatedAt;
    private Boolean isDeleted;
    private Date deletedAt;
}
