/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.*;
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
@Table(name = "card")
@XmlRootElement
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "hash")
    private String hash;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    @Column(name = "is_assigned")
    private Boolean isAssigned;

    @Column(name = "last_assigned_to")
    private Integer lastAssignedTo;
    @OneToOne(mappedBy = "cardId")
    @ToString.Exclude
    private Student student;


    public Card(Integer id) {
        this.id = id;
    }

}
