package com.radnoti.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;

import java.util.Date;

public interface WorkgroupscheduleRepository extends CrudRepository<Workgroupschedule, Integer> {
    @Procedure
    void createWorkgroupSchedule(String name, Date start, Date end, Boolean isOnsite);


}