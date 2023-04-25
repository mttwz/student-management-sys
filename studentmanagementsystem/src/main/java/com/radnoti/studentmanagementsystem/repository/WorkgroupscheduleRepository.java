package com.radnoti.studentmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public interface WorkgroupscheduleRepository extends CrudRepository<Workgroupschedule, Integer> {

    @Query("select ws from Workgroupschedule ws " +
            "where ws.workgroupId.id = :workgroupId and " +
            "ws.start like concat(:dateStr,'%')")
    Page<Workgroupschedule> getWorkgroupScheduleByWorkgroupId(Integer workgroupId, String dateStr ,Pageable pageable);

    @Query("select ws from Workgroupschedule ws " +
            "join Workgroupmembers wm on wm.workgroupId.id = ws.workgroupId.id " +
            "join Workgroup w on wm.workgroupId.id = w.id " +
            "where wm.userId.id = :userId ")
    Page<Workgroupschedule>getWorkgroupScheduleByUserId(Integer userId,Pageable pageable);

    @Query("select ws from Workgroupschedule ws " +
            "join Workgroupmembers wm on wm.workgroupId.id = ws.workgroupId.id " +
            "join Workgroup w on wm.workgroupId.id = w.id " +
            "where wm.userId.id = :userId " +
            "and ws.start like concat(:dateStr,'%')")
    Page<Workgroupschedule>getWorkgroupSchedulePerDayByUserId(Integer userId,String dateStr ,Pageable pageable);

    @Query("select ws from Workgroupschedule ws " +
            "join Workgroupmembers wm on wm.workgroupId.id = ws.workgroupId.id " +
            "join Workgroup w on wm.workgroupId.id = w.id " +
            "where wm.userId.id = :userId " +
            "and ws.start like concat(:dateStr,'%') " +
            "and w.id = :workgroupId")
    Page<Workgroupschedule>getWorkgroupSchedulePerDayPerWgByUserId(Integer userId, Integer workgroupId, String dateStr ,Pageable pageable);
}