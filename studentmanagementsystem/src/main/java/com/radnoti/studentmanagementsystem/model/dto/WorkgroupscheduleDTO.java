/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
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
public class WorkgroupscheduleDTO implements Serializable {

    private Integer id;

    private String name;

    private Integer workgroupId;

    private Date start;

    private Date end;

    private Boolean isOnsite;



    //private Collection<WorkgroupDTO> workgroupCollection;

    public WorkgroupscheduleDTO(Integer id) {
        this.id = id;
    }

    public WorkgroupscheduleDTO(Workgroupschedule workgroupschedule) {
        this.id = workgroupschedule.getId();
        this.name = workgroupschedule.getName();
        this.workgroupId = workgroupschedule.getWorkgroupId().getId();
        this.start = workgroupschedule.getStart();
        this.end = workgroupschedule.getEnd();
        this.isOnsite = workgroupschedule.getIsOnsite();
    }
}
