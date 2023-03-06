package com.radnoti.studentmanagementsystem.model.dto;

import com.radnoti.studentmanagementsystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {

    private Integer id;
    private String roleName;
    private String firstName;
    private String lastName;
    private String phone;
    private Date birth;
    private String email;
    private Date registeredAt;
    private Boolean isActivated;
    private Date activatedAt;
    private Boolean isDeleted;
    private Date deletedAt;


    public UserInfoDTO(User user) {
        this.id = user.getId();
        this.roleName = user.getRoleId().getRoleType();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.birth = user.getBirth();
        this.email = user.getEmail();
        this.registeredAt = user.getRegisteredAt();
        this.isActivated = user.getIsActivated();
        this.activatedAt = user.getActivatedAt();
        this.isDeleted = user.getIsDeleted();
        this.deletedAt = user.getDeletedAt();
    }
}
