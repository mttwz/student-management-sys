/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import java.util.*;

import com.radnoti.studentmanagementsystem.model.entity.User;
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
    User login(String email, String password);

    @Modifying
    @Query("update User u set u.roleId.id = " +
            "case when (select r.id from Role r where r.roleType = :roleName) = null then :#{T(com.radnoti.studentmanagementsystem.enums.RoleEnum).STUDENT.id} " +
            "else (select r.id from Role r where r.roleType = :roleName) end " +
            "where u.id = :userId")
    void setUserRole(Integer userId, String roleName);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByUsername(String email);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%'))")
    Page<User> searchAllUser(String searchString, Pageable pageable);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 3")
    Page<User>  searchStudents(String searchString,Pageable pageable);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 2")
    Page<User>  searchAdmins(String searchString,Pageable pageable);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 1")
    Page<User>  searchSuperadmins(String searchString,Pageable pageable);

    @Query("select u from User u " +
            "join Workgroupmembers wm on u.id = wm.userId.id " +
            "join Workgroup w on w.id = wm.workgroupId.id " +
            "where u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%')")
    Page<User>  searchWorkgroups(String searchString,Pageable pageable);

    @Procedure
    List<User> getUserFromWorkgroup(Integer id);



}
