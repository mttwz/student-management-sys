/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
@AllArgsConstructor
@Table(name = "User")
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    @Column(name = "birth")
    private ZonedDateTime birth;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Column(name = "registered_at")
    private ZonedDateTime registeredAt;
    @Lob
    @Size(max = 65535)
    @Column(name = "activation_code")
    private String activationCode;
    @Column(name = "is_activated")
    private Boolean isActivated;
    @Column(name = "activated_at")
    private ZonedDateTime activatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    @ToString.Exclude
    private Role roleId;
    @OneToMany(mappedBy = "userId")
    @ToString.Exclude
    private Collection<Workgroupmembers> workgroupmembersCollection;
    @OneToOne(mappedBy = "userId", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Student student;
    @OneToMany(mappedBy = "userId")
    @ToString.Exclude
    private Collection<Passwordreset> passwordresetCollection;


    public User(Integer id) {
        this.id = id;
    }

}
