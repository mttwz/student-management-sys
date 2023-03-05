package com.radnoti.studentmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO implements Serializable {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String jwt;



}
