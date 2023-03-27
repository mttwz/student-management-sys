/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

import java.time.ZonedDateTime;
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
public class AttendanceDto {

    private Integer id;

    private ZonedDateTime arrival;

    private ZonedDateTime leaving;

}
