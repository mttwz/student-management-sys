package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * Registers a user.
     *
     * @param userDto the user DTO containing the user details.
     * @return a ResponseDto object containing the registration response.
     * @throws NoSuchAlgorithmException if the hashing algorithm is not found.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/register-user")
    public ResponseDto adduser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return userService.adduser(userDto);
    }

    /**
     * Sets the activation status of a user.
     *
     * @param userDto the user DTO containing the user details.
     * @throws NoSuchAlgorithmException if the hashing algorithm is not found.
     */
    @PostMapping(path = "/set-user-is-activated")
    public void setUserIsActivated(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        userService.setUserIsActivated(userDto);
    }

    /**
     * Deletes a user.
     *
     * @param userId the ID of the user to be deleted.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @DeleteMapping(path = "/delete-user/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }


    /**
     * Restores a deleted user.
     *
     * @param userId the ID of the user to be restored.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/restore-deleted-user/{userId}")
    public void restoreDeletedUser(@PathVariable String userId) {
        userService.restoreDeletedUser(userId);
    }



    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of UserInfoDto objects representing all users.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @GetMapping(path = "/get-all-user")
    public  ResponseEntity<List<UserInfoDto>> getWorkgroupScheduleByUserId() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    /**
     * Retrieves the information of a user.
     *
     * @param userId the ID of the user.
     * @return a ResponseEntity containing the UserInfoDto object representing the user.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN,RoleEnum.Types.ADMIN})
    @GetMapping(path = "/get-user-info/{userId}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));

    }


    /**
     * Edits the information of a user.
     *
     * @param userId      the ID of the user.
     * @param userInfoDto the UserInfoDto object containing the updated user information.
     * @return a ResponseDto object containing the response.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/edit-user-info/{userId}", consumes = {"application/json"}, produces = {"application/json"})
    public @ResponseBody ResponseDto editUserInfo(@PathVariable String userId, @RequestBody UserInfoDto userInfoDto) {
        return userService.editUserInfo(userId,userInfoDto);
    }


    /**
     * Searches for super administrators based on the specified criteria.
     *
     * @param groupId   the ID of the group (optional).
     * @param category  the category of the search.
     * @param q         the search query.
     * @param pageable  the pagination information.
     * @return a PagingDto object containing the search results.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN})
    @PostMapping(path = "/search-super-admin")
    public @ResponseBody PagingDto searchSuperadmin(@RequestParam(required = false) String groupId, @RequestParam String category,
                                                    @RequestParam String q,
                                                    Pageable pageable){
        return userService.searchSuperadmin(groupId,category,q,pageable);
        
    }


    /**
     * Searches for administrators based on the specified criteria.
     *
     * @param groupId   the ID of the group (optional).
     * @param category  the category of the search.
     * @param q         the search query.
     * @param pageable  the pagination information.
     * @return a PagingDto object containing the search results.
     */
    @RolesAllowed({RoleEnum.Types.SUPERADMIN, RoleEnum.Types.ADMIN})
    @PostMapping(path = "/search-admin")
    public @ResponseBody PagingDto searchAdmin(@RequestParam(required = false) String groupId, @RequestParam String category,
                                                    @RequestParam String q,
                                                    Pageable pageable){
        return userService.searchAdmin(groupId,category,q,pageable);

    }



}
