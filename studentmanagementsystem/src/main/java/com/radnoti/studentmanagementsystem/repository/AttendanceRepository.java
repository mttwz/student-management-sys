package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttendanceRepository extends CrudRepository<Attendance, Integer> {
    @Query("select a from Attendance a " +
            "where a.studentId.id = :studentId " +
            "order by a.id desc")
    List<Attendance> getLastAttendanceByStudentId(Integer studentId, Pageable pageable);

    @Query("select a from Attendance a " +
            "where a.studentId.id = :studentId" )
    Page<Attendance> getAttendanceByStudentId(Integer studentId, Pageable pageable);


    @Query("select a from Attendance a " +
            "join Student s on s.id = a.studentId.id " +
            "join User u on s.userId.id = u.id " +
            "where u.id = :userId")
    Page<Attendance> getAttendanceByUserId(Integer userId, Pageable pageable);

    @Query("select a from Attendance a " +
            "join Student s on s.id = a.studentId.id " +
            "join User u on s.userId.id = u.id " +
            "where u.id = :userId and " +
            "a.arrival like concat(:dateStr,'%')")
    List<Attendance> getAttendancePerDayByUserId(Integer userId, String dateStr);

    @Query("select a from Attendance a " +
            "join Student s on s.id = a.studentId.id " +
            "join User u on s.userId.id = u.id " +
            "where u.id = :userId and " +
            "a.arrival like concat(:dateStr,'%')")
    Page<Attendance> getOwnAttendancePerDay(Integer userId, String dateStr,Pageable pageable);
}