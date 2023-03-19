/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupMembersMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.WorkgroupMembersRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import com.radnoti.studentmanagementsystem.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final int ACTIVATION_CODE_LENGTH = 8;

    private final UserRepository userRepository;

    private final WorkgroupscheduleRepository workgroupscheduleRepository;

    private final WorkgroupMembersRepository workgroupMembersRepository;

    private final WorkgroupRepository workgroupRepository;


    private final HashUtil hashUtil;

    private final UserMapper userMapper;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;
    private final WorkgroupMembersMapper workgroupMembersMapper;


    @Transactional
    public Integer adduser(UserDto userDto) throws NoSuchAlgorithmException {
        if ((userDto.getRoleName() == null ||
                userDto.getFirstName() == null ||
                userDto.getLastName() == null||
                userDto.getPhone() == null||
                userDto.getBirth().toString() == null ||
                userDto.getEmail() == null ||
                userDto.getPassword() == null ||
                userDto.getRoleName().isEmpty() ||
                userDto.getFirstName().isEmpty() ||
                userDto.getLastName().isEmpty() ||
                userDto.getPhone().isEmpty() ||
                userDto.getBirth().toString().isEmpty() ||
                userDto.getEmail().isEmpty() ||
                userDto.getPassword().isEmpty())) {
            throw new InvalidFormValueException();
        }

        userRepository.findByUsername(userDto.getEmail())
                .ifPresent(u -> {throw new UserAlreadyExistException();});

        int roleId;

        if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            roleId = RoleEnum.SUPERADMIN.getId();
        } else if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            roleId = RoleEnum.ADMIN.getId();
        } else roleId = RoleEnum.STUDENT.getId();

        User user = userMapper.fromDtoToEntity(userDto);
        user.setPassword(hashUtil.getSHA256Hash(userDto.getPassword()));
        user.setActivationCode(hashUtil.generateRandomString(ACTIVATION_CODE_LENGTH));
        user.setIsActivated(false);
        user.setIsDeleted(false);
        user.setRoleId(new Role(roleId));
        user.setRegisteredAt(Date.from(Instant.now()));

        User savedUser = userRepository.save(user);

        return savedUser.getId();

    }





    @Transactional
    public Integer addUserToWorkgroup(WorkgroupmembersDto workgroupmembersDto) {
        userRepository.findById(workgroupmembersDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        workgroupRepository.findById(workgroupmembersDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        Workgroupmembers workgroupmembers = workgroupMembersMapper.fromDtoToEntity(workgroupmembersDto);
        Workgroupmembers savedWorkgroupmembers =  workgroupMembersRepository.save(workgroupmembers);

        return savedWorkgroupmembers.getId();

    }

    @Transactional
    public Integer setUserIsActivated(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);
        if (user.getIsActivated()){
            throw new UserAlreadyActivatedException();
        }
        user.setIsActivated(true);

        return userDto.getId();
    }



    @Transactional
    public Integer deleteUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        if (user.getIsDeleted()) {
            throw new UserAlreadyDeletedException();
        }
        user.setIsDeleted(true);

        return userDto.getId();
    }

    @Transactional
    public Integer setUserRole(UserDto userDto) {
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);
        userRepository.setUserRole(userDto.getId(), userDto.getRoleName());
        return userDto.getId();
    }

    @Transactional
    public ArrayList<UserInfoDto> getAllUser() {
        Iterable<User> userIterable = userRepository.findAll();
        ArrayList<UserInfoDto> userInfoDtoArrayList = new ArrayList<>();
        for (User user : userIterable) {
            userInfoDtoArrayList.add(userMapper.fromEntityToInfoDto(user));
        }
        return userInfoDtoArrayList;
    }

    @Transactional
    public UserInfoDto getUserInfo(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        return userMapper.fromEntityToInfoDto(user);

    }

    @Transactional
    public Integer editUserInfo(String pathVariableUserId, UserInfoDto userInfoDto) {

        Integer userId;
        try{
            userId = Integer.parseInt(pathVariableUserId);
        }catch (NumberFormatException ex){
            throw new InvalidFormValueException();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);


        String userName = userInfoDto.getEmail();

        Optional<User> optionalUserByEmail = userRepository.findByUsername(userName);

        if (optionalUserByEmail.isPresent() && !Objects.equals(optionalUserByEmail.get().getId(), userId)) {
            throw new UserAlreadyExistException();
        }

        if (userInfoDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            user.setRoleId(new Role(RoleEnum.SUPERADMIN.getId()));
        } else if (userInfoDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            user.setRoleId(new Role(RoleEnum.ADMIN.getId()));
        } else user.setRoleId(new Role(RoleEnum.STUDENT.getId()));

        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        user.setBirth(userInfoDto.getBirth());
        user.setEmail(userInfoDto.getEmail());
        user.setPhone(userInfoDto.getPhone());
        userRepository.save(user);
        return userId;
    }

    @Transactional
    public ArrayList<UserDto> searchSuperadmin(SearchDto searchDto) {
        ArrayList<UserDto> userDtoArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = new ArrayList<>();

        if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.ALL_USERS)) {
            userArrayList = userRepository.searchAllUser(searchDto.getSearchText());
        } else if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.SUPERADMIN)) {
            userArrayList = userRepository.searchSuperadmins(searchDto.getSearchText());
        } else if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.STUDENT)) {
            userArrayList = userRepository.searchStudents(searchDto.getSearchText());
        } else if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.ADMIN)) {
            userArrayList = userRepository.searchAdmins(searchDto.getSearchText());
        } else if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.WORKGROUP)) {
            userArrayList = userRepository.searchWorkgroups(searchDto.getSearchText());
        } else if (Objects.equals(searchDto.getSearchFilter(), SearchFilterEnum.Types.INSTITUTION)) {
            userArrayList = userRepository.searchWorkgroups(searchDto.getSearchText());

        }

        for (int i = 0; i < userArrayList.size(); i++) {
            userDtoArrayList.add(userMapper.fromEntityToDto(userArrayList.get(i)));
        }
        return userDtoArrayList;
    }


    @Transactional
    public ArrayList<UserDto> getUserFromWorkgroup(UserDto userDto) {
        ArrayList<UserDto> userDtoArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = userRepository.getUserFromWorkgroup(userDto.getId());
        for (int i = 0; i < userArrayList.size(); i++) {
            userDtoArrayList.add(userMapper.fromEntityToDto(userArrayList.get(i)));
        }

        return userDtoArrayList;
    }


}
