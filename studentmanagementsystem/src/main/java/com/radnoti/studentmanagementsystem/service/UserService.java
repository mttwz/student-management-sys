/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.role.RoleNotExistException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
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
import java.time.ZonedDateTime;
import java.util.*;

import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final IdValidatorUtil idValidatorUtil;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;
    private final WorkgroupMembersMapper workgroupMembersMapper;


    /**
     * Creates a new user based on the given UserDto object and saves it to the database.
     * The method first checks if all required fields are present in the UserDto object, and if not, it throws an InvalidFormValueException.
     * It then checks if a user with the same email already exists in the database, and if so, it throws a UserAlreadyExistException.
     * The method sets the user's password to a hash value generated using the SHA-256 algorithm.
     * It also sets an activation code for the user, which is a randomly generated string of length ACTIVATION_CODE_LENGTH.
     * The user's activation status is set to false, and its deleted status is set to false.
     * The user's role is determined based on the roleName field of the UserDto object, which can be "superadmin", "admin", or "student".
     * The created user is then saved to the database and its ID is returned.
     *
     * @param userDto a Dto object containing the user's information
     * @return the ID of the created user
     * @throws InvalidFormValueException if a required field is missing or empty in the UserDto object
     * @throws UserAlreadyExistException if a user with the same email already exists in the database
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available in the environment
     */

    @Transactional
    public ResponseDto adduser(UserDto userDto) throws NoSuchAlgorithmException {
        if ((userDto.getRoleName() == null ||
                userDto.getFirstName() == null ||
                userDto.getLastName() == null||
                userDto.getPhone() == null||
                userDto.getBirth() == null ||
                userDto.getEmail() == null ||
                userDto.getPassword() == null ||
                userDto.getRoleName().isBlank() ||
                userDto.getFirstName().isBlank() ||
                userDto.getLastName().isBlank() ||
                userDto.getPhone().isBlank() ||
                userDto.getBirth().toString().isBlank() ||
                userDto.getEmail().isBlank() ||
                userDto.getPassword().isBlank())) {
            throw new InvalidFormValueException();
        }

        userRepository.findByUsername(userDto.getEmail())
                .ifPresent(u -> {throw new UserAlreadyExistException();});

        int roleId;

        if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            roleId = RoleEnum.SUPERADMIN.getId();
        } else if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            roleId = RoleEnum.ADMIN.getId();
        } else if (userDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.STUDENT)){
            roleId = RoleEnum.STUDENT.getId();
        }else throw new RoleNotExistException();


        
        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        User user = userMapper.fromDtoToEntity(userDto);
        user.setPassword(hashUtil.getSHA256Hash(userDto.getPassword()));
        user.setActivationCode(hashUtil.generateRandomString(ACTIVATION_CODE_LENGTH));
        user.setIsActivated(false);
        user.setIsDeleted(false);
        user.setRoleId(new Role(roleId));
        user.setRegisteredAt(currDate);

        User savedUser = userRepository.save(user);

        return new ResponseDto(savedUser.getId());

    }





    /**
     * Activates the user in the database based on the provided id.
     *
     * @param userIdString The user's id as String.
     * @throws UserNotExistException if the provided user's ID does not exist in the database.
     * @throws UserAlreadyActivatedException if the provided user already activated.
     *
     */
    @Transactional
    public void setUserIsActivated(String userIdString) {
        Integer userId = idValidatorUtil.idValidator(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);
        if (Boolean.TRUE.equals(user.getIsDeleted())){
            throw new UserAlreadyDeletedException();
        }
        if (Boolean.TRUE.equals(user.getIsActivated())){
            throw new UserAlreadyActivatedException();
        }
        user.setIsActivated(true);
    }




    @Transactional
    public void deleteUser(String userIdString) {
        Integer userId;
        try {
            userId = Integer.parseInt(userIdString);
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (user.getIsDeleted()) {
            throw new UserAlreadyDeletedException();
        }
        user.setIsDeleted(true);

    }



    @Transactional
    public List<UserInfoDto> getAllUser() {
        Iterable<User> userIterable = userRepository.findAll();
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        userIterable.forEach(user -> userInfoDtoList.add(userMapper.fromEntityToInfoDto(user)));
        return userInfoDtoList;
    }

    @Transactional
    public UserInfoDto getUserInfo(String userIdString) {
        Integer userId;
        try {
            userId = Integer.parseInt(userIdString);
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);
        return userMapper.fromEntityToInfoDto(user);

    }

    @Transactional
    public ResponseDto editUserInfo(String pathVariableUserId, UserInfoDto userInfoDto) {

        Integer userId;
        try{
            userId = Integer.parseInt(pathVariableUserId);
        }catch (NumberFormatException ex){
            throw new InvalidFormValueException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        Optional<User> optionalUserByEmail = userRepository.findByUsername(userInfoDto.getEmail());

        if (optionalUserByEmail.isPresent() && !Objects.equals(optionalUserByEmail.get().getId(), userId)) {
            throw new UserAlreadyExistException();
        }

        if (userInfoDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            user.setRoleId(new Role(RoleEnum.SUPERADMIN.getId()));
        } else if (userInfoDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            user.setRoleId(new Role(RoleEnum.ADMIN.getId()));
        } else if (userInfoDto.getRoleName().equalsIgnoreCase(RoleEnum.Types.STUDENT)){
            user.setRoleId(new Role(RoleEnum.STUDENT.getId()));
        }else throw new RoleNotExistException();

        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        user.setBirth(userInfoDto.getBirth());
        user.setEmail(userInfoDto.getEmail());
        user.setPhone(userInfoDto.getPhone());

        return new ResponseDto(userId);
    }

    @Transactional
    public PagingDto searchSuperadmin(String filter,String q,Pageable pageable) {


        Page<User> userPage = userRepository.searchAllUser(q,pageable);
        PagingDto pagingDto = new PagingDto();

        if (Objects.equals(filter, SearchFilterEnum.Types.SUPERADMIN)) {
            userPage = userRepository.searchSuperadmins(q,pageable);
        } else if (Objects.equals(filter, SearchFilterEnum.Types.STUDENT)) {
            userPage = userRepository.searchStudents(q,pageable);
        } else if (Objects.equals(filter, SearchFilterEnum.Types.ADMIN)) {
            userPage = userRepository.searchAdmins(q,pageable);
        } else if (Objects.equals(filter, SearchFilterEnum.Types.WORKGROUP)) {
            userPage = userRepository.searchWorkgroups(q,pageable);
        } else if (Objects.equals(filter, SearchFilterEnum.Types.INSTITUTION)) {
            userPage = userRepository.searchWorkgroups(q,pageable);
        }

        Integer totalPages = userPage.getTotalPages();

        pagingDto.setAllPages(totalPages);
        pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        return pagingDto;
    }

}
