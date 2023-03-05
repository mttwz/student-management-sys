/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import lombok.*;

import java.io.Serializable;
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
public class AttendanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Date arrival;

    private Date leaving;

    private StudentDTO studentId;


    public AttendanceDTO(Attendance attendance) {
        this.id = attendance.getId();
        this.arrival = attendance.getArrival();
        this.leaving = attendance.getLeaving();
        this.studentId = new StudentDTO(attendance.getStudentId());
    }
}
