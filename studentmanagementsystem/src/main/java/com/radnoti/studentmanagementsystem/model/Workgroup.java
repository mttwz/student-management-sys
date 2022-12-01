/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author matevoros
 */
@Entity
@Table(name = "workgroup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Workgroup.findAll", query = "SELECT w FROM Workgroup w"),
    @NamedQuery(name = "Workgroup.findById", query = "SELECT w FROM Workgroup w WHERE w.id = :id"),
    @NamedQuery(name = "Workgroup.findByGroupName", query = "SELECT w FROM Workgroup w WHERE w.groupName = :groupName"),
    @NamedQuery(name = "Workgroup.findByInstitution", query = "SELECT w FROM Workgroup w WHERE w.institution = :institution")})
public class Workgroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "group_name")
    private String groupName;
    @Size(max = 255)
    @Column(name = "institution")
    private String institution;
    @OneToMany(mappedBy = "workgroupId")
    private Collection<WorkgroupSchedule> workgroupScheduleCollection;
    @OneToMany(mappedBy = "workgroupId")
    private Collection<User> userCollection;

    public Workgroup() {
    }

    public Workgroup(Integer id) {
        this.id = id;
    }

    public Workgroup(Integer id, String groupName, String institution, Collection<WorkgroupSchedule> workgroupScheduleCollection, Collection<User> userCollection) {
        this.id = id;
        this.groupName = groupName;
        this.institution = institution;
        this.workgroupScheduleCollection = workgroupScheduleCollection;
        this.userCollection = userCollection;
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

    @XmlTransient
    public Collection<WorkgroupSchedule> getWorkgroupScheduleCollection() {
        return workgroupScheduleCollection;
    }

    public void setWorkgroupScheduleCollection(Collection<WorkgroupSchedule> workgroupScheduleCollection) {
        this.workgroupScheduleCollection = workgroupScheduleCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
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
        if (!(object instanceof Workgroup)) {
            return false;
        }
        Workgroup other = (Workgroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.radnoti.studentmanagementsystem.model.Workgroup[ id=" + id + " ]";
    }
    
}
