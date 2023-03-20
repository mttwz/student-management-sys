package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttendanceRepository extends CrudRepository<Attendance, Integer> {
    @Query("select a from Attendance a where a.studentId.id = :studentId order by a.id desc")
    List<Attendance> getLastAttendanceByStudentId(Integer studentId, Pageable pageable);
}