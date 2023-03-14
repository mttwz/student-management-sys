/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Collection;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
public class StudentDto {

    private Integer id;

    private Collection<AttendanceDto> attendanceCollection;

    private Integer cardId;

    private Integer userId;

}
