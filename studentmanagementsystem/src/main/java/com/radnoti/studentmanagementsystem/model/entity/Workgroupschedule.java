/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Table(name = "Workgroup_schedule")
@XmlRootElement
@AllArgsConstructor
public class Workgroupschedule implements Serializable {

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
    private ZonedDateTime start;
    @Column(name = "end")
    private ZonedDateTime end;
    @Column(name = "is_onsite")
    private Boolean isOnsite;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    @JoinColumn(name = "workgroup_id", referencedColumnName = "id")
    @ManyToOne
    @ToString.Exclude
    private Workgroup workgroupId;
    public Workgroupschedule(Integer id) {
        this.id = id;
    }

}
