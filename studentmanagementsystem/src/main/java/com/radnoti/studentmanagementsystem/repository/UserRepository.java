/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import java.util.*;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author matevoros
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Procedure
    Integer register(Integer roleId,String firstName, String lastName, String phone, Date birth, String email, String password);

    @Procedure
    int getUserIdByToken(String token);

    @Procedure
    int login(String email, String password);
    
    @Procedure
    void setUserIsActivated(Integer userId);
    
    @Procedure
    void setUserIsDeleted(Integer userId);
    
    @Procedure
    void setUserRole(Integer userId, String roleName);

    @Procedure
    Integer addUserToWorkgroup(Integer userId, Integer workgroupId);

    @Procedure
    ArrayList<Integer>getWorkgroupScheduleByUserId(Integer userId);

    @Procedure
    Optional<User> findByUsername(String email);

    @Procedure
    ArrayList<User> getAllUser();

    @Procedure
    ArrayList<User> searchAllUser(String string);

    @Procedure
    ArrayList<User> searchStudents(String string);

    @Procedure
    ArrayList<User> searchAdmins(String string);

    @Procedure
    ArrayList<User> searchSuperadmins(String string);

    @Procedure
    ArrayList<User> searchWorkgroups(String string);

    @Procedure
    ArrayList<User> getUserFromWorkgroup(Integer id);



}
