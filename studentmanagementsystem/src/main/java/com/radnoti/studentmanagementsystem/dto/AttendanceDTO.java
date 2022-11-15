/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author matevoros
 */

public class AttendanceDTO implements Serializable {

    private Integer id;

    private Date arrival;

    private Date leaving;

    private StudentDTO studentId;

    public AttendanceDTO() {
    }


}
