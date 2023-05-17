package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import com.radnoti.studentmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "${cross-origin}")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    /**
     * Authenticates a user and generates a login token.
     *     @param userDto The user information for authentication.
     *     @return  object containing the login token.
     *     @throws NoSuchAlgorithmException If the hashing algorithm for password encryption is not available.
    **/
    @PostMapping(path = "/login", consumes = {"application/json"}, produces = {"application/json"})
    public UserLoginDto login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {

        return authService.login(userDto);
    }

    /**
     * Validates a JSON Web Token (JWT) for a user.
     * @param userDto The user information containing the JWT to validate.
     * @return  indicating whether the JWT is valid or not.
     */
    @PostMapping(path = "/validate-jwt", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Boolean> validateJwt(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.validateJwt(userDto));
    }

}
