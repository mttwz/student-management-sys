package com.radnoti.studentmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;

import java.util.Date;

public interface WorkgroupscheduleRepository extends CrudRepository<Workgroupschedule, Integer> {

    @Query("select ws from Workgroupschedule ws where ws.workgroupId.id = :workgroupId")
    Page<Workgroupschedule> getWorkgroupScheduleByWorkgroupId(Integer workgroupId, Pageable pageable);

}