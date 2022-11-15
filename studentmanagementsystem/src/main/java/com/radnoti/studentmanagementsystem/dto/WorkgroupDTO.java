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

public class WorkgroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String groupName;

    private String institution;

    private Collection<WorkgroupscheduleDTO> workgroupscheduleCollection;

    private Collection<StudentDTO> studentCollection;

    public WorkgroupDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Collection<WorkgroupscheduleDTO> getWorkgroupscheduleCollection() {
        return workgroupscheduleCollection;
    }

    public void setWorkgroupscheduleCollection(Collection<WorkgroupscheduleDTO> workgroupscheduleCollection) {
        this.workgroupscheduleCollection = workgroupscheduleCollection;
    }

    public Collection<StudentDTO> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<StudentDTO> studentCollection) {
        this.studentCollection = studentCollection;
    }
}
