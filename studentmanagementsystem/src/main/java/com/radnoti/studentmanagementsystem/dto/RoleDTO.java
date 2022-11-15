/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.dto;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author matevoros
 */

public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roleType;

    private Collection<UserDTO> userCollection;

    public RoleDTO() {
    }


}
