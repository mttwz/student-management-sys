package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Role;
import com.radnoti.studentmanagementsystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
//
//    @Test
//    //@Sql(scripts={"classpath:sqls/AuthLogin.sql"})
//    public void setUserRoleTest_valid() {
//        //arrange
//        UserDto userDto = new UserDto();
//        userDto.setId(1);
//        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
//
//        Role role = new Role(1);
//        role.setRoleType(RoleEnum.Types.SUPERADMIN.toLowerCase());
//
//        User user = new User(1);
//        user.setRoleId(role);
//
//        //act
//        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
//
//        //assert
//        //assertEquals(1, userService.setUserRole(userDto));
//    }
//
//    @Test()
//    public void setUserRoleTest_empty() {
//        //arrange
//        UserDto userDto = new UserDto();
//        userDto.setId(1);
//        userDto.setRoleName(RoleEnum.Types.SUPERADMIN.toLowerCase());
//        //act
//        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
//        //assert
//        assertThrows(UserNotExistException.class, () -> roleService.setUserRole(userDto));
//    }
//

}
