/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.enums.SearchFilterEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.role.RoleNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.*;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.mapper.*;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.*;
import com.radnoti.studentmanagementsystem.repository.*;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.security.JwtUtil;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;

import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final WorkgroupRepository workgroupRepository;
    private final HashUtil hashUtil;
    private final UserMapper userMapper;
    private final CardMapper cardMapper;
    private final IdValidatorUtil idValidatorUtil;
    private final WorkgroupMapper workgroupMapper;
    private final CardRepository cardRepository;
    private final WorkgroupscheduleRepository workgroupscheduleRepository;


    /**
     * Creates a new user based on the given UserDto object and saves it to the database.
     * The method first checks if all required fields are present in the UserDto object, and if not, it throws an FormValueInvalidException.
     * It then checks if a user with the same email already exists in the database, and if so, it throws a UserAlreadyExistException.
     * The method sets the user's password to a hash value generated using the SHA-256 algorithm.
     * It also sets an activation code for the user, which is a randomly generated string of length ACTIVATION_CODE_LENGTH.
     * The user's activation status is set to false, and its deleted status is set to false.
     * The user's role is determined based on the roleName field of the UserDto object, which can be "superadmin", "admin", or "student".
     * The created user is then saved to the database and its ID is returned.
     *
     * @param userDto a Dto object containing the user's information
     * @return the ID of the created user
     * @throws FormValueInvalidException if a required field is missing or empty in the UserDto object
     * @throws UserAlreadyExistException if a user with the same email already exists in the database
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available in the environment
     * @throws RoleNotExistException If the role with the provided role name does not exist.
     */

    @Transactional
    public ResponseDto adduser(UserDto userDto) throws NoSuchAlgorithmException {
        if ((userDto.getRoleName() == null ||
                userDto.getFirstName() == null ||
                userDto.getLastName() == null||
                userDto.getPhone() == null||
                userDto.getBirth() == null ||
                userDto.getEmail() == null ||
                userDto.getRoleName().isBlank() ||
                userDto.getFirstName().isBlank() ||
                userDto.getLastName().isBlank() ||
                userDto.getPhone().isBlank() ||
                userDto.getBirth().toString().isBlank() ||
                userDto.getEmail().isBlank())) {
            throw new FormValueInvalidException();
        }

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()){
            userDto.setPassword(userDto.getFirstName()+userDto.getBirth().getYear());
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
     * Activates a user based on the provided UserDto object.
     * The method retrieves a user from the database using the activation code specified in the UserDto object.
     * It then verifies that the password provided in the UserDto object matches the stored password hash of the user.
     * If the password does not match or the user is already activated or deleted, appropriate exceptions are thrown.
     * The user's activated status is set to true and the activatedAt field is updated with the current date and time.
     *
     * @param userDto a Dto object containing the user's activation information
     * @throws NoSuchAlgorithmException     if the SHA-256 algorithm is not available in the environment
     * @throws UserNotActivatedException    if the user is not activated or the activation code is not found
     * @throws UserAlreadyDeletedException  if the user is already deleted
     * @throws UserAlreadyActivatedException if the user is already activated
     */
    @Transactional
    public void setUserIsActivated(UserDto userDto) throws NoSuchAlgorithmException {

        User userByCode = userRepository.findByActivationCode(userDto.getActivationCode())
                .orElseThrow(UserNotActivatedException::new);

        if (!Objects.equals(userByCode.getPassword(), hashUtil.getSHA256Hash(userDto.getPassword()))){
            throw new UserNotActivatedException();
        }

        if (Boolean.TRUE.equals(userByCode.getIsDeleted())){
            throw new UserAlreadyDeletedException();
        }
        if (Boolean.TRUE.equals(userByCode.getIsActivated())){
            throw new UserAlreadyActivatedException();
        }
        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        userByCode.setActivatedAt(currDate);

        userByCode.setIsActivated(true);
    }



    /**
     * Deletes a user from the database based on the provided user ID.
     * The method retrieves the user from the database using the user ID.
     * If the user is already deleted, a UserAlreadyDeletedException is thrown.
     * The user's deleted status is set to true and the deletedAt field is updated with the current date and time.
     *
     * @param userIdString a string representing the ID of the user to be deleted
     * @throws UserNotExistException        if the user with the provided ID does not exist in the database
     * @throws UserAlreadyDeletedException  if the user is already deleted
     */
    @Transactional
    public void deleteUser(String userIdString) {
        Integer userId = idValidatorUtil.idValidator(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (user.getIsDeleted()) {
            throw new UserAlreadyDeletedException();
        }
        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        user.setIsDeleted(true);
        user.setDeletedAt(currDate);

    }


    /**
     * Restores a previously deleted user in the database based on the provided user ID.
     * The method retrieves the user from the database using the user ID.
     * If the user is not deleted, a UserNotDeletedException is thrown.
     * The user's deleted status is set to false and the deletedAt field is set to null, indicating that the user is no longer deleted.
     *
     * @param userIdString a string representing the ID of the user to be restored
     * @throws UserNotExistException    if the user with the provided ID does not exist in the database
     * @throws UserNotDeletedException  if the user is not deleted
     */
    @Transactional
    public void restoreDeletedUser(String userIdString) {
        Integer userId = idValidatorUtil.idValidator(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (!user.getIsDeleted()) {
            throw new UserNotDeletedException();
        }
        user.setIsDeleted(false);
        user.setDeletedAt(null);

    }


    /**
     * Retrieves a list of all users from the database.
     * The method fetches all user entities from the database and maps them to UserInfoDto objects using a mapper.
     * The resulting list of UserInfoDto objects is returned.
     *
     * @return a list of UserInfoDto objects representing all users in the database
     */
    @Transactional
    public List<UserInfoDto> getAllUser() {
        Iterable<User> userIterable = userRepository.findAll();
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        userIterable.forEach(user -> userInfoDtoList.add(userMapper.fromEntityToInfoDto(user)));
        return userInfoDtoList;
    }


    /**
     * Retrieves the information of a user with the specified user ID.
     * The method fetches the user entity from the database based on the provided user ID.
     * If a user with the given ID is found, it is mapped to a UserInfoDto object using a mapper,
     * and the UserInfoDto object is returned.
     *
     * @param userIdString a string representing the ID of the user
     * @return a UserInfoDto object containing the information of the user
     * @throws UserNotExistException if a user with the specified ID does not exist in the database
     */
    @Transactional
    public UserInfoDto getUserInfo(String userIdString) {
        Integer userId = idValidatorUtil.idValidator(userIdString);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);
        return userMapper.fromEntityToInfoDto(user);

    }


    /**
     * Edits the information of a user with the specified user ID based on the provided UserInfoDto object.
     * The method retrieves the user entity from the database using the user ID.
     * If a user with the given ID is found, the method checks if the provided email is already associated with another user.
     * If so, it throws a UserAlreadyExistException.
     * The method then updates the user's role, first name, last name, date of birth, email, and phone number based on the values in the UserInfoDto object.
     * The user's role is determined based on the roleName field of the UserInfoDto object, which can be "superadmin", "admin", or "student".
     * Finally, the method returns a ResponseDto object containing the ID of the edited user.
     *
     * @param userIdString a string representing the ID of the user to be edited
     * @param userInfoDto  a UserInfoDto object containing the updated information of the user
     * @return a ResponseDto object containing the ID of the edited user
     * @throws UserNotExistException       if a user with the specified ID does not exist in the database
     * @throws UserAlreadyExistException   if the provided email is already associated with another user
     * @throws RoleNotExistException       if the roleName field of the UserInfoDto object does not match any valid role
     */
    @Transactional
    public ResponseDto editUserInfo(String userIdString, UserInfoDto userInfoDto) {

        Integer userId = idValidatorUtil.idValidator(userIdString);

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





    /**
     * Searches for superadmins, users, or workgroups based on the provided search criteria.
     * The method allows searching for different categories of data, such as all users, superadmins, students, admins, workgroups, or users within a specific workgroup.
     * The search is performed using the specified search query (q) and pageable parameters.
     * The method returns a PagingDto object containing the search results and pagination information.
     *
     * @param groupIdString a string representing the ID of the workgroup (used for searching users within a workgroup)
     * @param category      a string representing the category of data to search for (e.g., "all_users", "superadmin", "student", "admin", "workgroup", "users_in_workgroup")
     * @param q             a string representing the search query
     * @param pageable      a Pageable object defining the pagination parameters
     * @return a PagingDto object containing the search results and pagination information
     */
    @Transactional
    public PagingDto searchSuperadmin(String groupIdString, String category,String q,Pageable pageable) {

        Integer groupId = null;

        if (groupIdString != null){
            groupId = idValidatorUtil.idValidator(groupIdString);

        }

        Page<User> userPage;
        Page<Workgroup> workgroupPage;

        Integer totalPages = null;
        PagingDto pagingDto = new PagingDto();

        if(Objects.equals(category, SearchFilterEnum.Types.ALL_USERS)){
            userPage = userRepository.searchAllUser(q,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        } else if (Objects.equals(category, SearchFilterEnum.Types.SUPERADMIN)) {
            userPage = userRepository.searchSuperadmins(q,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        } else if (Objects.equals(category, SearchFilterEnum.Types.STUDENT)) {
            userPage = userRepository.searchStudents(q,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        } else if (Objects.equals(category, SearchFilterEnum.Types.ADMIN)) {
            userPage = userRepository.searchAdmins(q,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        } else if (Objects.equals(category, SearchFilterEnum.Types.WORKGROUP)) {
            workgroupPage = workgroupRepository.searchWorkgroups(q,pageable);
            totalPages = workgroupPage.getTotalPages();
            pagingDto.setWorkgroupDtoList(workgroupPage.stream().map(workgroupMapper::fromEntityToDto).toList());

        }else if (Objects.equals(category, SearchFilterEnum.Types.USERS_IN_WORKGROUP)) {
            userPage = userRepository.searchUsersInWorkgroups(q,groupId,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());
        }


        pagingDto.setAllPages(totalPages);


        return pagingDto;
    }


    /**
     * Searches for students, workgroups, or users within a specific workgroup based on the provided search criteria.
     * The method allows searching for different categories of data, such as students, workgroups, or users within a workgroup.
     * The search is performed using the specified search query (q) and pageable parameters.
     * The method returns a PagingDto object containing the search results and pagination information.
     *
     * @param groupIdString a string representing the ID of the workgroup (used for searching users within a workgroup)
     * @param category      a string representing the category of data to search for (e.g., "student", "workgroup", "users_in_workgroup")
     * @param q             a string representing the search query
     * @param pageable      a Pageable object defining the pagination parameters
     * @return a PagingDto object containing the search results and pagination information
     */

    @Transactional
    public PagingDto searchAdmin(String groupIdString, String category,String q,Pageable pageable) {

        Integer groupId = null;

        if (groupIdString != null){
            groupId = idValidatorUtil.idValidator(groupIdString);

        }

        Page<User> userPage;
        Page<Workgroup> workgroupPage;

        Integer totalPages = null;
        PagingDto pagingDto = new PagingDto();

        if (Objects.equals(category, SearchFilterEnum.Types.STUDENT)) {
            userPage = userRepository.searchStudents(q,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());

        }else if (Objects.equals(category, SearchFilterEnum.Types.WORKGROUP)) {
            workgroupPage = workgroupRepository.searchWorkgroups(q,pageable);
            totalPages = workgroupPage.getTotalPages();
            pagingDto.setWorkgroupDtoList(workgroupPage.stream().map(workgroupMapper::fromEntityToDto).toList());

        }else if (Objects.equals(category, SearchFilterEnum.Types.USERS_IN_WORKGROUP)) {
            userPage = userRepository.searchUsersInWorkgroups(q,groupId,pageable);
            totalPages = userPage.getTotalPages();
            pagingDto.setUserInfoDtoList(userPage.stream().map(userMapper::fromEntityToInfoDto).toList());
        }


        pagingDto.setAllPages(totalPages);


        return pagingDto;
    }





}
