/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author matevoros
 */
@Entity
@Table(name = "Workgroup_members")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Workgroupmembers.findAll", query = "SELECT w FROM Workgroupmembers w"),
    @NamedQuery(name = "Workgroupmembers.findById", query = "SELECT w FROM Workgroupmembers w WHERE w.id = :id")})
public class Workgroupmembers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @JoinColumn(name = "workgroup_id", referencedColumnName = "id")
    @ManyToOne
    private Workgroup workgroupId;

    public Workgroupmembers() {
    }

    public Workgroupmembers(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Workgroupmembers)) {
            return false;
        }
        Workgroupmembers other = (Workgroupmembers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.radnoti.studentmanagementsystem.model.Workgroupmembers[ id=" + id + " ]";
    }
    
}
