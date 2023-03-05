package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    private final DateFormatUtil dateFormatUtil;

    @Transactional
    public String updateJwt(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        String jwt = "";
        if (optionalUser.isPresent()) {
            jwt = jwtConfig.generateJwt(optionalUser.get());
        }
        return jwt;
    }

    @Transactional
    public UserLoginDTO login(UserDTO userDTO){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        int userId = userRepository.login(userDTO.getEmail(), userDTO.getPassword());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent() && optionalUser.get().getIsActivated() && !optionalUser.get().getIsDeleted()) {
            userLoginDTO.setId(userId);
            userLoginDTO.setEmail(optionalUser.get().getEmail());
            userLoginDTO.setJwt(jwtConfig.generateJwt(optionalUser.get()));
            userLoginDTO.setFirstName(optionalUser.get().getFirstName());
            userLoginDTO.setLastName(optionalUser.get().getLastName());
            return userLoginDTO;
        }

        throw new UsernameNotFoundException("Invalid");

    }


    @Transactional
    public Map validateJwt(UserDTO userDTO) {
        HashMap<String, Boolean> map = new HashMap<>();
        boolean isValid = jwtConfig.validateJwt(userDTO.getJwt());
        map.put("valid", isValid);
        return map;
    }
}
