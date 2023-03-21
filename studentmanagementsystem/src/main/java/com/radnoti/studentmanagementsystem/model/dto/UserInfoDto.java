package com.radnoti.studentmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDto {

    private Integer id;
    private String roleName;
    private String firstName;
    private String lastName;
    private String phone;
    private ZonedDateTime birth;
    private String email;
    private ZonedDateTime registeredAt;
    private Boolean isActivated;
    private ZonedDateTime activatedAt;
    private Boolean isDeleted;
    private ZonedDateTime deletedAt;
}
