/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.Student;
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
public class StudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Collection<AttendanceDTO> attendanceCollection;

    private Integer cardId;

    private Integer userId;



    public StudentDTO(Student student) {
        this.id = student.getId();
        this.cardId = student.getCardId().getId();
        this.userId = student.getUserId().getId();

    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", attendanceCollection=" + attendanceCollection +
                ", cardId=" + cardId +
                ", userId=" + userId +
                '}';
    }
}
