/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 *
 * @author matevoros
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkgroupDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String groupName;

    private String institution;

    private UserDto userId;

    private WorkgroupscheduleDto scheduleId;

}