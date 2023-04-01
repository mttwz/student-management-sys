package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    private final HashUtil hashUtil;

    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     * @param userDto The user DTO containing the user's email and password.
     * @return A UserLoginDto object containing the user's information and JWT token.
     * @throws InvalidCredentialsException If the email or password is invalid.
     * @throws UserNotActivatedException If the user has not activated their account.
     * @throws UserDeletedException If the user has been deleted.
     * @throws NoSuchAlgorithmException If the SHA256 algorithm is not available.
     */

    @Transactional
    public UserLoginDto login(UserDto userDto) throws NoSuchAlgorithmException {
        if (userDto.getEmail() == null ||
                userDto.getPassword() == null ||
                userDto.getEmail().isBlank() ||
                userDto.getPassword().isBlank()){
            throw new FormValueInvalidException();
        }
        String password = hashUtil.getSHA256Hash(userDto.getPassword());

        User user = userRepository.login(userDto.getEmail(), password)
                .orElseThrow(InvalidCredentialsException::new);

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

    /**
     * Validates a JWT token provided in the UserDto object.
     * @param userDto The UserDto object containing the JWT token to be validated.
     * @return A Map object containing a "valid" key with a Boolean value indicating whether the token is valid or not.
     */
    @Transactional
    public Boolean validateJwt(UserDto userDto) {
        return jwtUtil.validateJwt(userDto.getJwt());
    }
}
