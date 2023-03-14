package com.radnoti.studentmanagementsystem.service;


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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid username or password");
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid username or password");
        }
        if (!optionalUser.get().getIsActivated()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not activated");
        }
        if(optionalUser.get().getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deleted");
        }

        UserLoginDto userLoginDto = userMapper.fromEntityToLoginDto(optionalUser.get());
        userLoginDto.setJwt(jwtUtil.generateJwt(optionalUser.get()));

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
