/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author matevoros
 */
@Entity
@Table(name = "workgroup_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkgroupSchedule.findAll", query = "SELECT w FROM WorkgroupSchedule w"),
    @NamedQuery(name = "WorkgroupSchedule.findById", query = "SELECT w FROM WorkgroupSchedule w WHERE w.id = :id"),
    @NamedQuery(name = "WorkgroupSchedule.findByName", query = "SELECT w FROM WorkgroupSchedule w WHERE w.name = :name"),
    @NamedQuery(name = "WorkgroupSchedule.findByStart", query = "SELECT w FROM WorkgroupSchedule w WHERE w.start = :start"),
    @NamedQuery(name = "WorkgroupSchedule.findByEnd", query = "SELECT w FROM WorkgroupSchedule w WHERE w.end = :end"),
    @NamedQuery(name = "WorkgroupSchedule.findByIsOnsite", query = "SELECT w FROM WorkgroupSchedule w WHERE w.isOnsite = :isOnsite")})
public class WorkgroupSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;
    @Column(name = "end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    @Column(name = "is_onsite")
    private Boolean isOnsite;
    @JoinColumn(name = "workgroup_id", referencedColumnName = "id")
    @ManyToOne
    private Workgroup workgroupId;

    public WorkgroupSchedule() {
    }

    public WorkgroupSchedule(Integer id) {
        this.id = id;
    }

    public WorkgroupSchedule(Integer id, String name, Date start, Date end, Boolean isOnsite, Workgroup workgroupId) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.isOnsite = isOnsite;
        this.workgroupId = workgroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Boolean getIsOnsite() {
        return isOnsite;
    }

    public void setIsOnsite(Boolean isOnsite) {
        this.isOnsite = isOnsite;
    }

    public Workgroup getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Workgroup workgroupId) {
        this.workgroupId = workgroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkgroupSchedule)) {
            return false;
        }
        WorkgroupSchedule other = (WorkgroupSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.radnoti.studentmanagementsystem.model.WorkgroupSchedule[ id=" + id + " ]";
    }
    
}
