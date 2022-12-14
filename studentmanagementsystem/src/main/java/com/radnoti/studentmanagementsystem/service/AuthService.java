package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final DateFormatUtil dateFormatUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, DateFormatUtil dateFormatUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.dateFormatUtil = dateFormatUtil;
    }

    @Transactional
    public String updateJwt(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        String jwt = "";
        if (optionalUser.isPresent()) {
            jwt = jwtUtil.generateJwt(optionalUser);
        }
        return jwt;
    }

    @Transactional
    public UserDTO.UserLoginDTO login(UserDTO userDTO){
        UserDTO.UserLoginDTO userLoginDTO = new UserDTO.UserLoginDTO();
        int userId = userRepository.login(userDTO.getEmail(), userDTO.getPassword());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userLoginDTO.setId(userId);
            userLoginDTO.setEmail(optionalUser.get().getEmail());
            userLoginDTO.setJwt(jwtUtil.generateJwt(optionalUser));
            userLoginDTO.setFirstName(optionalUser.get().getFirstName());
            userLoginDTO.setLastName(optionalUser.get().getLastName());
        }
        return userLoginDTO;
    }



    @Transactional
    public Map validateJwt(UserDTO userDTO) {
        HashMap<String, String> map = new HashMap<>();
        boolean isValid = jwtUtil.validateJwt(userDTO.getJwt());
        map.put("valid", String.valueOf(isValid));
        return map;
    }
}
