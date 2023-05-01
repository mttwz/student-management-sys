/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import java.util.*;

import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author matevoros
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from User u " +
            "where u.email = :email and " +
            "u.password = :password")
    Optional<User> login(String email, String password);

    //= null then :#{T(com.radnoti.studentmanagementsystem.enums.RoleEnum).STUDENT.id}
    @Modifying
    @Query("update User u set u.roleId.id = " +
            "(select r.id from Role r where r.roleType = :roleName) " +
            "where u.id = :userId")
    void setUserRole(Integer userId, String roleName);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByUsername(String email);

    @Query("select u from User u where u.activationCode = :activationCode")
    Optional<User> findByActivationCode(String activationCode);

    @Query("select u from User u " +
            "join Passwordreset pwr on u.id = pwr.userId.id " +
            "where pwr.resetCode = :resetCode")
    Optional<User> findByResetCode(String resetCode);

    @Query("select u from User u where " +
            ":userSearchString is null or " +
            "(u.firstName like concat(:userSearchString,'%') or " +
            "u.lastName like concat(:userSearchString,'%') or " +
            "u.email like concat(:userSearchString,'%'))")
    Page<User> searchAllUser(String userSearchString, Pageable pageable);

    @Query("select u from User u where " +
            ":userSearchString is null or " +
            "(u.firstName like concat(:userSearchString,'%') or " +
            "u.lastName like concat(:userSearchString,'%') or " +
            "u.email like concat(:userSearchString,'%')) and " +
            "u.roleId.id = :#{T(com.radnoti.studentmanagementsystem.enums.RoleEnum).STUDENT.id}")
    Page<User>  searchStudents(String userSearchString,Pageable pageable);

    @Query("select u from User u where " +
            ":userSearchString is null or " +
            "(u.firstName like concat(:userSearchString,'%') or " +
            "u.lastName like concat(:userSearchString,'%') or " +
            "u.email like concat(:userSearchString,'%')) and " +
            "u.roleId.id = :#{T(com.radnoti.studentmanagementsystem.enums.RoleEnum).ADMIN.id}")
    Page<User>  searchAdmins(String userSearchString,Pageable pageable);
    //test

    @Query("select u from User u where " +
            ":userSearchString is null or " +
            "(u.firstName like concat(:userSearchString,'%') or " +
            "u.lastName like concat(:userSearchString,'%') or " +
            "u.email like concat(:userSearchString,'%')) and " +
            "u.roleId.id = :#{T(com.radnoti.studentmanagementsystem.enums.RoleEnum).SUPERADMIN.id}")
    Page<User>  searchSuperadmins(String userSearchString,Pageable pageable);

    @Query("select u from User u " +
            "join Workgroupmembers wm on wm.userId.id = u.id " +
            "join Workgroup w on w.id = wm.workgroupId.id " +
            "where :userSearchString is null or " +
            "(u.firstName like concat('%',:userSearchString,'%') or " +
            "u.lastName like concat('%',:userSearchString,'%') or " +
            "u.email like concat('%',:userSearchString,'%')) and " +
            "w.id = :groupId")
    Page<User> searchUsersInWorkgroups(String userSearchString, Integer groupId, Pageable pageable);



    @Query("select u from User u " +
            "where " +
            "(u.firstName like concat('%',:userSearchString,'%') or " +
            "u.lastName like concat('%',:userSearchString,'%') or " +
            "u.email like concat('%',:userSearchString,'%')) " +
            "and not exists (" +
            "select wm from Workgroupmembers wm " +
            "where wm.userId.id = u.id " +
            "and wm.workgroupId.id = :workgroupId) ")
    Page<User> getAllAddableUsers(String userSearchString, Integer workgroupId, Pageable pageable);



}
