/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.WorkgroupMembersRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
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


    private final UserRepository userRepository;

    private final WorkgroupscheduleRepository workgroupscheduleRepository;


    private final WorkgroupRepository workgroupRepository;

    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;


    @Transactional
    public Integer adduser(UserDto userDto) {

        if ((userDto.getRoleName() == null || userDto.getFirstName() == null || userDto.getLastName() == null|| userDto.getPhone() == null|| userDto.getBirth().toString() == null || userDto.getEmail() == null || userDto.getPassword() == null)) {
            throw new NullFormValueException();
        }

        userRepository.findByUsername(userDto.getEmail())
                .ifPresent(u -> {throw new UserAlreadyExistException();});


        if ((userDto.getRoleName().isEmpty() || userDto.getFirstName().isEmpty() || userDto.getLastName().isEmpty() || userDto.getPhone().isEmpty() || userDto.getBirth().toString().isEmpty() || userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty())) {
            throw new EmptyFormValueException();
        }

        int roleId;

        if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            roleId = RoleEnum.SUPERADMIN.getId();
        } else if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            roleId = RoleEnum.ADMIN.getId();
        } else roleId = RoleEnum.STUDENT.getId();

        Integer savedUserId = userRepository.register(roleId, userDto.getFirstName(), userDto.getLastName(), userDto.getPhone(), userDto.getBirth(), userDto.getEmail(), userDto.getPassword());

        return savedUserId;

    }


    @Transactional
    public ArrayList<WorkgroupscheduleDto> getWorkgroupScheduleByUserId(String authHeader, UserDto userDto) {
        ArrayList<Integer> workgroupScheduleList;
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        if (jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDto.getId());
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromJwt(authHeader));
        }

        ArrayList<WorkgroupscheduleDto> workgroupscheduleDtoArrayList = new ArrayList<>();
        Iterable<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findAllById(workgroupScheduleList);

        for(Workgroupschedule workgroupschedule : optionalWorkgroupschedule){
            workgroupscheduleDtoArrayList.add(workgroupScheduleMapper.fromEntityToDto(workgroupschedule));
        }
        return workgroupscheduleDtoArrayList;
    }


    @Transactional
    public Integer addUserToWorkgroup(WorkgroupmembersDto workgroupmembersDto) {
        userRepository.findById(workgroupmembersDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        workgroupRepository.findById(workgroupmembersDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        Integer savedWorkgroupMembersId = userRepository.addUserToWorkgroup(workgroupmembersDto.getUserId(), workgroupmembersDto.getWorkgroupId());
        if (savedWorkgroupMembersId == null){
            throw new UserNotAddedToWorkgroupException();
        }
        return savedWorkgroupMembersId;

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
        userRepository.setUserIsDeleted(userDto.getId());
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
            throw new InvalidIdException();
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
