/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import com.radnoti.studentmanagementsystem.repository.UserRepository;


import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.print.DocFlavor;

/**
 * @author matevoros
 */
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final WorkgroupRepository workgroupRepository;

    private final CardService cardService;


    private final JwtConfig jwtConfig;


    private final DateFormatUtil dateFormatUtil;


    @Transactional
    public Integer adduser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getEmail());
        if (optionalUser.isEmpty()) {
            if ((userDTO.getFirstName().isEmpty() || userDTO.getLastName().isEmpty() || userDTO.getPhone().isEmpty() || userDTO.getBirth().toString().isEmpty() || userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
            }
            int roleId;
            if (Objects.equals(userDTO.getRoleName(), "superadmin")) {
                roleId = 1;
            } else if (Objects.equals(userDTO.getRoleName(), "admin")) {
                roleId = 2;
            } else roleId = 3;
            return userRepository.register(roleId, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
        } else throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");


//        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getEmail());
//        if(optionalUser.isEmpty()){
//            if((userDTO.getFirstName().isEmpty() || userDTO.getLastName().isEmpty() || userDTO.getPhone().isEmpty() || userDTO.getBirth().toString().isEmpty() || userDTO.getEmail().isEmpty()||userDTO.getPassword().isEmpty()) ){
//                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
//            }
//            int roleId;
//            if(Objects.equals(userDTO.getRoleName(), "superadmin")){
//                roleId = 1;
//            }else if(Objects.equals(userDTO.getRoleName(), "admin")){
//                roleId = 2;
//            } else roleId = 3;
//            userRepository.register(roleId, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.getBirth(), userDTO.getEmail(), userDTO.getPassword());
//        }else throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");


    }

    @Transactional
    public ArrayList<WorkgroupscheduleDTO> getWorkgroupScheduleByUserId(String authHeader, UserDTO userDTO) {
        ArrayList<ArrayList<String>> workgroupScheduleList;
        if (jwtConfig.getRoleFromJwt(authHeader).equals("admin")) {
            System.out.println("asd");
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtConfig.getIdFromJwt(authHeader));
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDTO.getId());
        }

        ArrayList<WorkgroupscheduleDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < workgroupScheduleList.size(); i++) {
            WorkgroupscheduleDTO workgroupscheduleDTO = new WorkgroupscheduleDTO();
            workgroupscheduleDTO.setId(Integer.parseInt(workgroupScheduleList.get(i).get(0)));
            workgroupscheduleDTO.setName(workgroupScheduleList.get(i).get(1));
            workgroupscheduleDTO.setStart(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(2)));
            workgroupscheduleDTO.setEnd(dateFormatUtil.dateFormatter(workgroupScheduleList.get(i).get(3)));
            workgroupscheduleDTO.setIsOnsite(Boolean.valueOf(workgroupScheduleList.get(i).get(4)));
            dtoList.add(workgroupscheduleDTO);
        }
        return dtoList;
    }

    @Transactional
    public Integer addUserToWorkgroup(WorkgroupmembersDTO workgroupmembersDTO) {
        Integer workgroupMembersId = userRepository.addUserToWorkgroup(workgroupmembersDTO.getUserId(), workgroupmembersDTO.getWorkgroupId());
        Optional<User> optionalUser = userRepository.findById(workgroupmembersDTO.getUserId());
        if (optionalUser.isPresent() && optionalUser.get().getWorkgroupmembersCollection().contains(new Workgroupmembers(workgroupMembersId))) {
            return workgroupMembersId;
        } else throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "NEMJO");
    }


    @Transactional
    public Optional<User> getUserData(UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getId());
        return user;
    }

    @Transactional
    public Integer setUserIsActivated(UserDTO userDTO) {
        userRepository.setUserIsActivated(userDTO.getId());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getIsActivated(), true)) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "NEMJO");
    }

    @Transactional
    public Integer deleteUser(UserDTO userDTO) {
        userRepository.setUserIsDeleted(userDTO.getId());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getIsDeleted(), true)) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "NEMJO");
    }

    @Transactional
    public Integer setUserRole(UserDTO userDTO) {
        userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getRoleId().getRoleType(), userDTO.getRoleName())) {
            return userDTO.getId();
        } else throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "NEMJO");
    }

    @Transactional
    public ArrayList<UserDTO> getAllUser() {

        Iterable<User> userIterable = userRepository.findAll();
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();
        for (User u : userIterable) {
            UserDTO userDTO = new UserDTO(u);
            userDTOArrayList.add(userDTO);
        }
        return userDTOArrayList;
    }

    @Transactional
    public UserDTO getUserInfo(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            //if(Objects.equals(optionalUser.get().getRoleId().getRoleType(), "student")){
            //    UserDTO userDTOWithCard = new UserDTO(optionalUser.get());
            //    System.err.println(cardService.getUserCard(userDTOWithCard).getId());
            //    userDTOWithCard.setCardId(cardService.getUserCard(userDTOWithCard).getId());
            //    return userDTOWithCard;
            //}
            return new UserDTO(optionalUser.get());
        }
        return userDTO;


    }

    @Transactional
    public void editUserInfo(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            if (Objects.equals(userDTO.getRoleName(), "superadmin")) {
                optionalUser.get().setRoleId(new Role(1));
            } else if (Objects.equals(userDTO.getRoleName(), "admin")) {
                optionalUser.get().setRoleId(new Role(2));
            } else optionalUser.get().setRoleId(new Role(3));
            try {

                optionalUser.get().setFirstName(userDTO.getFirstName());
                optionalUser.get().setLastName(userDTO.getLastName());
                optionalUser.get().setBirth(userDTO.getBirth());
                optionalUser.get().setEmail(userDTO.getEmail());
                optionalUser.get().setPhone(userDTO.getPhone());
                userRepository.save(optionalUser.get());
            } catch (IncorrectResultSizeDataAccessException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
            }
        }
    }

    @Transactional
    public ArrayList<UserDTO> searchSuperadmin(SearchDTO searchDTO) {
        ArrayList<UserDTO> userDTOArrayList = new ArrayList<>();

        if (Objects.equals(searchDTO.getSearchFilter(), "All users")) {
            ArrayList<User> userArrayList = userRepository.searchAllUser(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
        } else if (Objects.equals(searchDTO.getSearchFilter(), "Superadmin")) {
            ArrayList<User> userArrayList = userRepository.searchSuperadmins(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
        } else if (Objects.equals(searchDTO.getSearchFilter(), "Student")) {
            ArrayList<User> userArrayList = userRepository.searchStudents(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
        } else if (Objects.equals(searchDTO.getSearchFilter(), "Admin")) {
            ArrayList<User> userArrayList = userRepository.searchAdmins(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
        } else if (Objects.equals(searchDTO.getSearchFilter(), "Workgroup")) {
            ArrayList<User> userArrayList = userRepository.searchWorkgroups(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
        } else if (Objects.equals(searchDTO.getSearchFilter(), "Institution")) {
            ArrayList<User> userArrayList = userRepository.searchWorkgroups(searchDTO.getSearchText());
            for (int i = 0; i < userArrayList.size(); i++) {
                UserDTO actualUserDto = new UserDTO(userArrayList.get(i));
                userDTOArrayList.add(actualUserDto);
            }
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
