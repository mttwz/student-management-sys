package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
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

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    private final IdValidatorUtil idValidatorUtil;


    @Transactional
    public ResponseDto logStudent(String studentIdString){
        Integer studentId = idValidatorUtil.idValidator(studentIdString);

        Pageable pageable = PageRequest.of(0, 1);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotExistException::new);

        if (student.getUserId().getIsDeleted()){
            throw new UserDeletedException();
        }

        if (!student.getUserId().getIsActivated()){
            throw new UserNotActivatedException();
        }

        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        List<Attendance> lastAttendanceList = attendanceRepository.getLastAttendanceByStudentId(studentId,pageable);

        if(!lastAttendanceList.isEmpty()){
            Attendance lastAttendance = lastAttendanceList.get(0);
            ZonedDateTime lastArrival = lastAttendance.getArrival();
            ZonedDateTime lastLeaving = lastAttendance.getLeaving();
            if(lastLeaving == null && isSameDay(currDate,lastArrival)){
                lastAttendance.setLeaving(currDate);
                attendanceRepository.save(lastAttendance);
                return new ResponseDto(lastAttendance.getId());
            }
        }

        Attendance newAttendance = new Attendance();
        newAttendance.setArrival(currDate);
        newAttendance.setStudentId(student);
        attendanceRepository.save(newAttendance);
        return new ResponseDto(newAttendance.getId());

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
