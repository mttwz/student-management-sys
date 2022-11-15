package com.radnoti.studentmanagementsystem.repository;

import com.radnoti.studentmanagementsystem.model.Attendance;
import org.springframework.data.repository.CrudRepository;

public interface AttendanceRepository extends CrudRepository<Attendance, Integer> {
}