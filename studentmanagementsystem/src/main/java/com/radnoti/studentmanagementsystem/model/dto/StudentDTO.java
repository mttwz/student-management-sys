/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Collection<AttendanceDTO> attendanceCollection;

    private Integer cardId;

    private Integer userId;

    private Integer workgroupId;

}
