package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    /**
     * Generates a JWT token for the authenticated user
     * @param userDto The login credentials for the user in Json format
     * @return The authenticated user details in Json format.
     * @throws ResponseStatusException on failed login
     */

    @Transactional
    public UserLoginDto login(UserDto userDto){
        Integer userId = userRepository.login(userDto.getEmail(), userDto.getPassword());

        if(userId == null){
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findById(userId).orElseThrow(InvalidCredentialsException::new);


        if (!user.getIsActivated()){
            throw new UserNotActivatedException();
        }
        if(user.getIsDeleted()){
            throw new UserDeletedException();
        }

        UserLoginDto userLoginDto = userMapper.fromEntityToLoginDto(user);
        userLoginDto.setJwt(jwtUtil.generateJwt(user));

        return userLoginDto;
    }


    @Transactional
    public Map validateJwt(UserDto userDto) {
        HashMap<String, Boolean> map = new HashMap<>();
        boolean isValid = jwtUtil.validateJwt(userDto.getJwt());
        map.put("valid", isValid);
        return map;
    }
}
