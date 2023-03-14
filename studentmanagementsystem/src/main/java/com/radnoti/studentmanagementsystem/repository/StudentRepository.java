/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Student;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 *
 * @author matevoros
 */
public interface StudentRepository extends CrudRepository<Student, Integer>  {


    @Procedure
    Integer registerStudent(String firstName, String lastName, String phone, Date birth, String email, String password);

    @Procedure
    Integer logStudent(Integer studentId);

}
