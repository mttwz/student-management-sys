package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserInfoDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    @Mapping(source = "roleId.roleType",target = "roleName")
    UserDto fromEntityToDto(User user);

    @Mapping(source = "roleName",target = "roleId.roleType")
    User fromDtoToEntity(UserDto userDto);

    @Mapping(target = "jwt", ignore = true)
    UserLoginDto fromEntityToLoginDto(User user);
    @Mapping(source = "roleId.roleType", target = "roleName")
    UserInfoDto fromEntityToInfoDto(User user);


}
