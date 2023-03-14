/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.UserException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final WorkgroupscheduleRepository workgroupscheduleRepository;


    private final WorkgroupRepository workgroupRepository;

    private final WorkgroupMembersRepository workgroupMembersRepository;


    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;


    public User userExistanceCheck(Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, UserException.NOT_FOUND.getException());
        }
        return optionalUser.get();
    }



    @Transactional
    public Integer adduser(UserDto userDto) {

        if ((userDto.getRoleName() == null || userDto.getFirstName() == null || userDto.getLastName() == null|| userDto.getPhone() == null|| userDto.getBirth().toString() == null || userDto.getEmail() == null || userDto.getPassword() == null)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getEmail());
        if(optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, UserException.EXIST.getException());
        }

        if ((userDto.getRoleName().isEmpty() || userDto.getFirstName().isEmpty() || userDto.getLastName().isEmpty() || userDto.getPhone().isEmpty() || userDto.getBirth().toString().isEmpty() || userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is empty");
        }

        int roleId;
        if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            roleId = RoleEnum.SUPERADMIN.getId();
        } else if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            roleId = RoleEnum.ADMIN.getId();
        } else roleId = RoleEnum.STUDENT.getId();

        Integer savedUserId = userRepository.register(roleId, userDto.getFirstName(), userDto.getLastName(), userDto.getPhone(), userDto.getBirth(), userDto.getEmail(), userDto.getPassword());

        Optional<User> savedOptionalUser = userRepository.findById(savedUserId);

        if (savedOptionalUser.isEmpty() || !Objects.equals(savedOptionalUser.get().getEmail(), userDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not saved");
        }
        return savedUserId;

    }


    @Transactional
    public ArrayList<WorkgroupscheduleDto> getWorkgroupScheduleByUserId(String authHeader, UserDto userDto) {
        ArrayList<Integer> workgroupScheduleList;
        userExistanceCheck(userDto.getId());


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
        userExistanceCheck(workgroupmembersDto.getUserId());

        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupmembersDto.getWorkgroupId());
        if (optionalWorkgroup.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Workgroup not exist");
        }


        Integer savedWorkgroupMembersId = userRepository.addUserToWorkgroup(workgroupmembersDto.getUserId(), workgroupmembersDto.getWorkgroupId());
        if (savedWorkgroupMembersId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not added to workgroup");
        }

        Optional<Workgroupmembers> optionalWorkgroupmembers = workgroupMembersRepository.findById(savedWorkgroupMembersId);

        if (optionalWorkgroupmembers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not added to workgroup");
        }

        return savedWorkgroupMembersId;

    }


    @Transactional
    public Integer setUserIsActivated(UserDto userDto) {
        User user = userExistanceCheck(userDto.getId());
        userRepository.setUserIsActivated(userDto.getId());
        if (!Objects.equals(user.getIsActivated(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not activated");
        }
        return userDto.getId();
    }



    @Transactional
    public Integer deleteUser(UserDto userDto) {
        User user = userExistanceCheck(userDto.getId());
        userRepository.setUserIsDeleted(userDto.getId());
        if (!Objects.equals(user.getIsDeleted(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not deleted");
        }
        return userDto.getId();
    }

    @Transactional
    public Integer setUserRole(UserDto userDto) {
        User user = userExistanceCheck(userDto.getId());

        userRepository.setUserRole(userDto.getId(), userDto.getRoleName());

        if (!Objects.equals(user.getRoleId().getRoleType(), userDto.getRoleName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role not set");

        }
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
        User user = userExistanceCheck(userDto.getId());
        return userMapper.fromEntityToInfoDto(user);

    }

    @Transactional
    public Integer editUserInfo(String pathVariableUserId, UserInfoDto userInfoDto) {

        Integer userId;
        try{
            userId = Integer.parseInt(pathVariableUserId);
        }catch (NumberFormatException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid id");
        }
        User user = userExistanceCheck(userId);


        String userName = userInfoDto.getEmail();

        Optional<User> optionalUserByEmail = userRepository.findByUsername(userName);

        if (optionalUserByEmail.isPresent() && !Objects.equals(optionalUserByEmail.get().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
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
