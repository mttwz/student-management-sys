package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.role.RoleNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.repository.RoleRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void setUserRole(UserDto userDto) {
        if (userDto.getId() == null || userDto.getRoleName() == null || userDto.getRoleName().isBlank()){
            throw new FormValueInvalidException();
        }
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        roleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(RoleNotExistException::new);

        userRepository.setUserRole(userDto.getId(), userDto.getRoleName());
    }
}
