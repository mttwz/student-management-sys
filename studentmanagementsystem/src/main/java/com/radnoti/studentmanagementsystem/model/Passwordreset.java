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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author matevoros
 */
@Entity
@Table(name = "Password_reset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Passwordreset.findAll", query = "SELECT p FROM Passwordreset p"),
    @NamedQuery(name = "Passwordreset.findById", query = "SELECT p FROM Passwordreset p WHERE p.id = :id"),
    @NamedQuery(name = "Passwordreset.findByResetCode", query = "SELECT p FROM Passwordreset p WHERE p.resetCode = :resetCode"),
    @NamedQuery(name = "Passwordreset.findByExpireDate", query = "SELECT p FROM Passwordreset p WHERE p.expireDate = :expireDate"),
    @NamedQuery(name = "Passwordreset.findByIsUsed", query = "SELECT p FROM Passwordreset p WHERE p.isUsed = :isUsed")})
public class Passwordreset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reset_code")
    private String resetCode;
    @Column(name = "expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    @Basic(optional = false)
    @Column(name = "is_used")
    private boolean isUsed;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;

    public Passwordreset() {
    }

    public Passwordreset(Integer id) {
        this.id = id;
    }

    public Passwordreset(Integer id, boolean isUsed) {
        this.id = id;
        this.isUsed = isUsed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof Passwordreset)) {
            return false;
        }
        Passwordreset other = (Passwordreset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.radnoti.studentmanagementsystem.model.Passwordreset[ id=" + id + " ]";
    }
    
}
