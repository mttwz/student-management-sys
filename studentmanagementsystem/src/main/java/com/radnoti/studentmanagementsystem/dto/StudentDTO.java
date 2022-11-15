/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.dto;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author matevoros
 */

public class StudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Collection<AttendanceDTO> attendanceCollection;

    private Integer cardId;

    private Integer userId;

    private Integer workgroupId;

    private Collection<PasswordresetDTO> passwordresetCollection;

    public StudentDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<AttendanceDTO> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<AttendanceDTO> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public Collection<PasswordresetDTO> getPasswordresetCollection() {
        return passwordresetCollection;
    }

    public void setPasswordresetCollection(Collection<PasswordresetDTO> passwordresetCollection) {
        this.passwordresetCollection = passwordresetCollection;
    }
}
