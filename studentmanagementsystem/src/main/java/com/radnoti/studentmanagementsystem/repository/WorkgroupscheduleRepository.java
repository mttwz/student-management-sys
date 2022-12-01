package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.WorkgroupSchedule;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Date;

public interface WorkgroupscheduleRepository extends CrudRepository<WorkgroupSchedule, Integer> {
    @Procedure
    void createWorkgroupSchedule(String name, Integer workgroupId, Date start, Date end, Boolean isOnsite);


}