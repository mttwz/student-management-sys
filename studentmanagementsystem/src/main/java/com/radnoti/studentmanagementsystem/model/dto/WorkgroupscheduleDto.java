/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

import java.io.Serializable;
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
@AllArgsConstructor
public class WorkgroupscheduleDto implements Serializable {

    private Integer id;

    private String name;

    private Integer workgroupId;

    private ZonedDateTime start;

    private ZonedDateTime end;

    private Boolean isOnsite;

    private Boolean isDeleted;

    private ZonedDateTime deletedAt;



    //private Collection<WorkgroupDto> workgroupCollection;


}
