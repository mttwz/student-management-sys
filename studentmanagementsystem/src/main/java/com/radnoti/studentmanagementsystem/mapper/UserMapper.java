package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserInfoDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    @Mapping(source = "roleId.roleType",target = "roleName")
    UserDTO fromEntityToDto(User user);

    @Mapping(target = "jwt", ignore = true)
    UserLoginDTO fromEntityToLoginDto(User user);
    @Mapping(source = "roleId.roleType", target = "roleName")
    UserInfoDTO fromEntityToInfoDto(User user);


}
