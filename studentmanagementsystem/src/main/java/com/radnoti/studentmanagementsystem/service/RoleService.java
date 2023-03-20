package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;

    @Transactional
    public void setUserRole(UserDto userDto) {
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);
        userRepository.setUserRole(userDto.getId(), userDto.getRoleName());
    }
}
