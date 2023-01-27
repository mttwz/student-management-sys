/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

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
@AllArgsConstructor
public class WorkgroupscheduleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Date start;

    private Date end;

    private Boolean isOnsite;

    private Integer workgroupId;

    public WorkgroupscheduleDTO(Integer id) {
        this.id = id;
    }

}
