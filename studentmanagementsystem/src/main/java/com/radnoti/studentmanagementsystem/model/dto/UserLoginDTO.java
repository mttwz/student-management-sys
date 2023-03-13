package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDTO implements Serializable {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String jwt;





}
