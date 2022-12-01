/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author matevoros
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Procedure
    boolean validateRole(Integer userId,Integer roleId);
    
    @Procedure
    boolean validateJwt(Integer userId, String jwt);
    
    @Procedure
    int register(String firstName, String lastName, String phone, Date birth, String email, String password);
    
    //@Procedure
    //void updateJwt(Integer userId, String jwt);
    
    @Procedure
    int getUserIdByToken(String token);
    
    @Procedure
    void registerStudent(String firstName, String lastName, String phone, Date birth, String email, String password);
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
    ArrayList<ArrayList<String>> getWorkgroupScheduleByUserId(Integer userId);


    
}
