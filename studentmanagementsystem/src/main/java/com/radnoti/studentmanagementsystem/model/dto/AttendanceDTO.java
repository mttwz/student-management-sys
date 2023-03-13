/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;
import java.util.Date;

/**
 *
 * @author matevoros
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
@Data
public class AttendanceDTO{

    private Integer id;

    private Date arrival;

    private Date leaving;

    private StudentDTO studentId;

}
