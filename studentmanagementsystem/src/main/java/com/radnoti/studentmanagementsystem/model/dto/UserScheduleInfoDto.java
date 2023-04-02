/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Data
public class UserScheduleInfoDto {

    private Integer id;

    private Integer userId;

    private ZonedDateTime date;

    private String name;

    private Integer workgroupId;

    private ZonedDateTime start;

    private ZonedDateTime end;

    private Boolean isOnsite;

    private Boolean isDeleted;

    private ZonedDateTime deletedAt;

    private Boolean isStudentPresent;

    private Long lateInMinutes = 0L;



    public void updateLateInMinutes(Long lateInMinutes) {
        this.lateInMinutes += lateInMinutes;
    }
}
