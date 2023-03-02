/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 *
 * @author matevoros
 */
public interface WorkgroupRepository extends CrudRepository<Workgroup, Integer> {
    
    @Procedure
    void createWorkgroup(String groupName, String institution);

//    @Procedure
//    ArrayList<Workgroup> getAllUserIdFromWorkgroup(Integer workgroupId);
    
}
