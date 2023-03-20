/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import java.util.*;

import com.radnoti.studentmanagementsystem.model.entity.User;
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

    @Query("select u.id from User u " +
            "where u.email = :email and " +
            "u.password = :password")
    Integer login(String email, String password);

    @Modifying
    @Query("update User u set u.roleId.id = " +
            "case when (select r.id from Role r where r.roleType = :roleName) = null then 3 " +
            "else (select r.id from Role r where r.roleType = :roleName) end " +
            "where u.id = :userId")
    void setUserRole(Integer userId, String roleName);


    @Query("select ws.id from Workgroupmembers wm " +
            "join Workgroupschedule ws on wm.workgroupId.id = ws.workgroupId.id " +
            "join Workgroup w on wm.workgroupId.id = w.id " +
            "where wm.userId.id = :userId")
    ArrayList<Integer>getWorkgroupScheduleByUserId(Integer userId);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByUsername(String email);

    @Query("select u from User u where " +
            "u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')")
    ArrayList<User> searchAllUser(String searchString);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 3")
    ArrayList<User> searchStudents(String searchString);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 2")
    ArrayList<User> searchAdmins(String searchString);

    @Query("select u from User u where " +
            "(u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%') or " +
            "u.email like concat('%',:searchString,'%')) and " +
            "u.roleId.id = 1")
    ArrayList<User> searchSuperadmins(String searchString);

    @Query("select u from User u " +
            "join Workgroupmembers wm on u.id = wm.userId.id " +
            "join Workgroup w on w.id = wm.workgroupId.id " +
            "where u.firstName like concat('%',:searchString,'%') or " +
            "u.lastName like concat('%',:searchString,'%')")
    ArrayList<User> searchWorkgroups(String searchString);

    @Procedure
    ArrayList<User> getUserFromWorkgroup(Integer id);



}
