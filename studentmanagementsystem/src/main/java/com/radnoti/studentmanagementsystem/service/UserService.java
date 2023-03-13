/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupscheduleMapper;
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

    private final WorkgroupscheduleMapper workgroupscheduleMapper;


    public User userExistanceCheck(Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not exist");
        }
        return optionalUser.get();
    }



    @Transactional
    public Integer adduser(UserDTO userDTO) {

        if ((userDTO.getRoleName() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null|| userDTO.getPhone() == null|| userDTO.getBirth().toString() == null || userDTO.getEmail() == null || userDTO.getPassword() == null)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getEmail());
        if(optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }

        if ((userDTO.getRoleName().isEmpty() || userDTO.getFirstName().isEmpty() || userDTO.getLastName().isEmpty() || userDTO.getPhone().isEmpty() || userDTO.getBirth().toString().isEmpty() || userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is empty");
        }

        int roleId;
        if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            roleId = RoleEnum.SUPERADMIN.getId();
        } else if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            roleId = RoleEnum.ADMIN.getId();
        } else roleId = RoleEnum.STUDENT.getId();

        Integer savedUserId = userRepository.register(roleId, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
        Optional<User> savedOptionalUser = userRepository.findById(savedUserId);

        if (savedOptionalUser.isEmpty() || !Objects.equals(savedOptionalUser.get().getEmail(), userDTO.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not saved");
        }
        return savedUserId;

    }


    @Transactional
    public ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(String authHeader, UserDTO userDTO) {
        ArrayList<Integer> workgroupScheduleList;
        userExistanceCheck(userDTO.getId());


        if (jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDTO.getId());
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromJwt(authHeader));
        }

        ArrayList<WorkgroupscheduleDTO> workgroupscheduleDTOArrayList = new ArrayList<>();
        Iterable<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findAllById(workgroupScheduleList);

        for(Workgroupschedule workgroupschedule : optionalWorkgroupschedule){
            workgroupscheduleDTOArrayList.add( workgroupscheduleMapper.fromEntityToDto(workgroupschedule));
        }
        return workgroupscheduleDTOArrayList;
    }


    @Transactional
    public Integer addUserToWorkgroup(WorkgroupmembersDTO workgroupmembersDTO) {
        userExistanceCheck(workgroupmembersDTO.getUserId());

        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupmembersDTO.getWorkgroupId());
        if (optionalWorkgroup.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Workgroup not exist");
        }


        Integer savedWorkgroupMembersId = userRepository.addUserToWorkgroup(workgroupmembersDTO.getUserId(), workgroupmembersDTO.getWorkgroupId());
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
    public Integer setUserIsActivated(UserDTO userDTO) {
        User user = userExistanceCheck(userDTO.getId());
        userRepository.setUserIsActivated(userDTO.getId());
        if (!Objects.equals(user.getIsActivated(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not activated");
        }
        return userDTO.getId();
    }



    @Transactional
    public Integer deleteUser(UserDTO userDTO) {
        User user = userExistanceCheck(userDTO.getId());
        userRepository.setUserIsDeleted(userDTO.getId());
        if (!Objects.equals(user.getIsDeleted(), true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not deleted");
        }
        return userDTO.getId();
    }

    @Transactional
    public Integer setUserRole(UserDTO userDTO) {
        User user = userExistanceCheck(userDTO.getId());

        userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());

        if (!Objects.equals(user.getRoleId().getRoleType(), userDTO.getRoleName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role not set");

        }
        return userDTO.getId();
    }

    @Transactional
    public ArrayList<UserInfoDTO> getAllUser() {
        Iterable<User> userIterable = userRepository.findAll();
        ArrayList<UserInfoDTO> userInfoDTOArrayList = new ArrayList<>();
        for (User user : userIterable) {
            userInfoDTOArrayList.add(userMapper.fromEntityToInfoDto(user));
        }
        return userInfoDTOArrayList;
    }

    @Transactional
    public UserInfoDTO getUserInfo(UserDTO userDTO) {
        User user = userExistanceCheck(userDTO.getId());
        return userMapper.fromEntityToInfoDto(user);

    }

    @Transactional
    public Integer editUserInfo(String pathVariableUserId, UserInfoDTO userInfoDTO) {

        Integer userId;
        try{
            userId = Integer.parseInt(pathVariableUserId);
        }catch (NumberFormatException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid id");
        }
        User user = userExistanceCheck(userId);


        String userName = userInfoDTO.getEmail();

        Optional<User> optionalUserByEmail = userRepository.findByUsername(userName);

        if (optionalUserByEmail.isPresent() && !Objects.equals(optionalUserByEmail.get().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        if (userInfoDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            user.setRoleId(new Role(RoleEnum.SUPERADMIN.getId()));
        } else if (userInfoDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            user.setRoleId(new Role(RoleEnum.ADMIN.getId()));
        } else user.setRoleId(new Role(RoleEnum.STUDENT.getId()));

        user.setFirstName(userInfoDTO.getFirstName());
        user.setLastName(userInfoDTO.getLastName());
        user.setBirth(userInfoDTO.getBirth());
        user.setEmail(userInfoDTO.getEmail());
        user.setPhone(userInfoDTO.getPhone());
        userRepository.save(user);
        return userId;
    }

    @Transactional
    public ArrayList<UserDTO> searchSuperadmin(SearchDTO searchDTO) {
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = new ArrayList<>();

        if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.ALL_USERS)) {
            userArrayList = userRepository.searchAllUser(searchDTO.getSearchText());
        } else if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.SUPERADMIN)) {
            userArrayList = userRepository.searchSuperadmins(searchDTO.getSearchText());
        } else if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.STUDENT)) {
            userArrayList = userRepository.searchStudents(searchDTO.getSearchText());
        } else if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.ADMIN)) {
            userArrayList = userRepository.searchAdmins(searchDTO.getSearchText());
        } else if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.WORKGROUP)) {
            userArrayList = userRepository.searchWorkgroups(searchDTO.getSearchText());
        } else if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.INSTITUTION)) {
            userArrayList = userRepository.searchWorkgroups(searchDTO.getSearchText());

        }

        for (int i = 0; i < userArrayList.size(); i++) {
            userDTOArrayList.add(userMapper.fromEntityToDto(userArrayList.get(i)));
        }
        return userDTOArrayList;
    }


    @Transactional
    public ArrayList<UserDTO> getUserFromWorkgroup(UserDTO userDTO) {
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = userRepository.getUserFromWorkgroup(userDTO.getId());
        for (int i = 0; i < userArrayList.size(); i++) {
            userDTOArrayList.add(userMapper.fromEntityToDto(userArrayList.get(i)));
        }

        return userDTOArrayList;
    }


}
