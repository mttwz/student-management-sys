/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.Student;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 *
 * @author matevoros
 */
public interface StudentRepository extends CrudRepository<Student, Integer>  {
    
    @Procedure
    void connectCardToStudent(Integer studentId, Integer cardId);
    

    
    @Procedure
    void connectStudentToUser(Integer studentId, Integer userId);
    
    @Procedure
    void logStudent(Integer studentId);


    
}
