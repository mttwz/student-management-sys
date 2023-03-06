/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import com.radnoti.studentmanagementsystem.repository.UserRepository;


import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NonUniqueResultException;
import javax.print.DocFlavor;
import javax.validation.constraints.Null;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final WorkgroupscheduleRepository workgroupscheduleRepository;

    private final JwtConfig jwtConfig;

    private final DateFormatUtil dateFormatUtil;


    @Transactional
    public Integer adduser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getEmail());
        if (optionalUser.isEmpty()) {
            try {
                if ((userDTO.getFirstName().isEmpty() || userDTO.getLastName().isEmpty() || userDTO.getPhone().isEmpty() || userDTO.getBirth().toString().isEmpty() || userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty())) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
                }
            }catch (NullPointerException e){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
            }

            int roleId;
            if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
                roleId = RoleEnum.SUPERADMIN.getId();
            } else if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
                roleId = RoleEnum.ADMIN.getId();
            } else roleId = RoleEnum.STUDENT.getId();

            int userId = userRepository.register(roleId, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
            Optional<User> savedOptionalUser = userRepository.findById(userId);
            if (savedOptionalUser.isPresent() && Objects.equals(savedOptionalUser.get().getEmail(), userDTO.getEmail())){
                return userId;
            }else throw new ResponseStatusException(HttpStatus.CONFLICT, "User not saved");
        }else throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
    }

    @Transactional
    public ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(String authHeader, UserDTO userDTO) {
        ArrayList<Integer> workgroupScheduleList;
        if (jwtConfig.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDTO.getId());
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtConfig.getIdFromJwt(authHeader));
        }
        ArrayList<WorkgroupscheduleDTO> workgroupscheduleDTOArrayList = new ArrayList<>();
        Iterable<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findAllById(workgroupScheduleList);

        for(Workgroupschedule workgroupschedule : optionalWorkgroupschedule){
            WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO(workgroupschedule);
            workgroupscheduleDTOArrayList.add(workgroupscheduleDTO);
        }
        return workgroupscheduleDTOArrayList;
    }

    @Transactional
    public Integer addUserToWorkgroup(WorkgroupmembersDTO workgroupmembersDTO) {
        try{
            Integer workgroupMembersId = userRepository.addUserToWorkgroup(workgroupmembersDTO.getUserId(), workgroupmembersDTO.getWorkgroupId());
            Optional<User> optionalUser = userRepository.findById(workgroupmembersDTO.getUserId());
            if (optionalUser.isPresent() && optionalUser.get().getWorkgroupmembersCollection().contains(new Workgroupmembers(workgroupMembersId))) {
                return workgroupMembersId;
            } else throw new ResponseStatusException(HttpStatus.CONFLICT, "User not added to workgroup");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or workgroup does not exist");
        }
    }

    @Transactional
    public Integer setUserIsActivated(UserDTO userDTO) {
        userRepository.setUserIsActivated(userDTO.getId());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getIsActivated(), true)) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
    }

    @Transactional
    public Integer deleteUser(UserDTO userDTO) {
        userRepository.setUserIsDeleted(userDTO.getId());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getIsDeleted(), true)) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
    }

    @Transactional
    public Integer setUserRole(UserDTO userDTO) {
        userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getRoleId().getRoleType(), userDTO.getRoleName())) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
    }

    @Transactional
    public ArrayList<UserInfoDTO> getAllUser() {
        Iterable<User> userIterable = userRepository.findAll();
        ArrayList<UserInfoDTO> userInfoDTOArrayList = new ArrayList<>();
        for (User u : userIterable) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(u);
            userInfoDTOArrayList.add(userInfoDTO);
        }
        return userInfoDTOArrayList;
    }

    @Transactional
    public UserInfoDTO getUserInfo(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            return new UserInfoDTO(optionalUser.get());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");



    }

    @Transactional
    public Integer editUserInfo(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {

            int userId = optionalUser.get().getId();
            String userName = userDTO.getEmail();

            try {
                Optional<User> optionalUserByEmail = userRepository.findByUsername(userName);
                if (optionalUserByEmail.isPresent() && optionalUserByEmail.get().getId() != userId){
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
                }
            }catch (UsernameNotFoundException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
            }catch (NonUniqueResultException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Multiple results");
            }catch (NoSuchElementException ignored){}


            if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
                optionalUser.get().setRoleId(new Role(RoleEnum.SUPERADMIN.getId()));
            } else if (userDTO.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
                optionalUser.get().setRoleId(new Role(RoleEnum.ADMIN.getId()));
            } else optionalUser.get().setRoleId(new Role(RoleEnum.STUDENT.getId()));


            optionalUser.get().setFirstName(userDTO.getFirstName());
            optionalUser.get().setLastName(userDTO.getLastName());
            optionalUser.get().setBirth(userDTO.getBirth());
            optionalUser.get().setEmail(userDTO.getEmail());
            optionalUser.get().setPhone(userDTO.getPhone());
            userRepository.save(optionalUser.get());
            return userId;

        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
    }

    @Transactional
    public ArrayList<UserDTO> searchSuperadmin(SearchDTO searchDTO) {
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = new ArrayList<>();

        if (Objects.equals(searchDTO.getSearchFilter(), SearchFilterEnum.Types.ALL_USERS)) {
            userRepository.searchAllUser(searchDTO.getSearchText());
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
            UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
            userDTOArrayList.add(actualUserDto);
        }

        return userDTOArrayList;
    }

    @Transactional
    public ArrayList<UserDTO> getUserFromWorkgroup(UserDTO userDTO) {
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = userRepository.getUserFromWorkgroup(userDTO.getId());
        for (int i = 0; i < userArrayList.size(); i++) {
            System.out.println(userArrayList.get(i));
            UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
            userDTOArrayList.add(actualUserDto);
//            System.out.println(userDTOArrayList);
        }

        return userDTOArrayList;
    }


}
