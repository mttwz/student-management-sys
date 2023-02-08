/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import java.util.*;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author matevoros
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Procedure
    int register(String firstName, String lastName, String phone, Date birth, String email, String password);

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
    void addUserToWorkgroup(Integer userId, Integer workgroupId);

    @Procedure
    ArrayList<ArrayList<String>>getWorkgroupScheduleByUserId(Integer userId);

    @Procedure
    Optional<User> findByUsername(String email);

    @Procedure
    ArrayList<User> getAllUser();
}
