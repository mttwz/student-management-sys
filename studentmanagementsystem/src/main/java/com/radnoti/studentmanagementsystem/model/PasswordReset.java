/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author matevoros
 */
@Entity
@Table(name = "password_reset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PasswordReset.findAll", query = "SELECT p FROM PasswordReset p"),
    @NamedQuery(name = "PasswordReset.findById", query = "SELECT p FROM PasswordReset p WHERE p.id = :id"),
    @NamedQuery(name = "PasswordReset.findByResetCode", query = "SELECT p FROM PasswordReset p WHERE p.resetCode = :resetCode"),
    @NamedQuery(name = "PasswordReset.findByExpireDate", query = "SELECT p FROM PasswordReset p WHERE p.expireDate = :expireDate"),
    @NamedQuery(name = "PasswordReset.findByIsUsed", query = "SELECT p FROM PasswordReset p WHERE p.isUsed = :isUsed")})
public class PasswordReset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "reset_code")
    private String resetCode;
    @Column(name = "expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    @Column(name = "is_used")
    private Boolean isUsed;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;

    public PasswordReset() {
    }

    public PasswordReset(Integer id) {
        this.id = id;
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

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
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
        if (!(object instanceof PasswordReset)) {
            return false;
        }
        PasswordReset other = (PasswordReset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.radnoti.studentmanagementsystem.model.PasswordReset[ id=" + id + " ]";
    }
    
}
