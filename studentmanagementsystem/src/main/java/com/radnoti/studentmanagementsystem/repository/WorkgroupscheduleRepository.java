package com.radnoti.studentmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;

import java.util.Date;

public interface WorkgroupscheduleRepository extends CrudRepository<Workgroupschedule, Integer> {

    @Query("select ws from Workgroupschedule ws " +
            "where ws.workgroupId.id = :workgroupId")
    Page<Workgroupschedule> getWorkgroupScheduleByWorkgroupId(Integer workgroupId, Pageable pageable);

    @Query("select ws from Workgroupmembers wm " +
            "join Workgroupschedule ws on wm.workgroupId.id = ws.workgroupId.id " +
            "join Workgroup w on wm.workgroupId.id = w.id " +
            "where wm.userId.id = :userId")
    Page<Workgroupschedule>getWorkgroupScheduleByUserId(Integer userId,Pageable pageable);

}