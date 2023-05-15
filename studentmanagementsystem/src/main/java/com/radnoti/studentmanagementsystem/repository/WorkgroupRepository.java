/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matevoros
 */
public interface WorkgroupRepository extends CrudRepository<Workgroup, Integer> {

    @Query("select w from Workgroup w where " +
            "w.groupName like concat('%',:searchString,'%') or " +
            "w.institution like concat('%',:searchString,'%')")
    Page<Workgroup> searchWorkgroups(String searchString, Pageable pageable);

    @Query("select w from Workgroup w")
    Page<Workgroup> getAllWorkgroup(Pageable pageable);




    
}
