package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttendanceService {


    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;


    @Transactional
    public Integer logStudent(StudentDto studentDto){
        Pageable pageable = PageRequest.of(0, 1);

        Student student = studentRepository.findById(studentDto.getId()).orElseThrow(StudentNotExistException::new);

        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        List<Attendance> lastAttendanceList = attendanceRepository.getLastAttendanceByStudentId(studentDto.getId(),pageable);

        if(!lastAttendanceList.isEmpty()){
            Attendance lastAttendance = lastAttendanceList.get(0);
            ZonedDateTime lastArrival = lastAttendance.getArrival();
            ZonedDateTime lastLeaving = lastAttendance.getLeaving();
            if(lastLeaving == null && isSameDay(currDate,lastArrival)){
                lastAttendance.setLeaving(currDate);
                attendanceRepository.save(lastAttendance);
                return lastAttendance.getId();
            }
        }

        Attendance newAttendance = new Attendance();
        newAttendance.setArrival(currDate);
        newAttendance.setStudentId(student);
        attendanceRepository.save(newAttendance);
        return newAttendance.getId();

    }


    public static boolean isSameDay(ZonedDateTime date1, ZonedDateTime date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}
