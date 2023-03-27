package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
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


import java.security.NoSuchAlgorithmException;
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

    private final IdValidatorUtil idValidatorUtil;
    private final CardRepository cardRepository;


    /**
     * This method logs the attendance of a student with the given student ID.
     * The method retrieves the student with the given ID from the database, validates the student's status,
     * and creates an attendance record with the current date and time. The method also checks if there is an
     * existing attendance record for the student on the same day, and if so, updates it with the leaving time.
     *
     * @param studentIdString a string representing the ID of the student
     * @return a ResponseDto object containing the ID of the created or updated attendance record
     * @throws InvalidIdException  if the provided student id is invalid eg: if the id contains a string
     * @throws StudentNotExistException  if the student with the given ID does not exist in the database
     * @throws UserDeletedException      if the student's user account has been deleted
     * @throws UserNotActivatedException if the student's user account has not been activated
     */
    @Transactional
    public ResponseDto logStudent(String cardHash) {

        System.err.println(cardHash);

        Card card = cardRepository.findByHash(cardHash)
                .orElseThrow(CardNotExistException::new);
        System.err.println(card.getId());
        Pageable pageable = PageRequest.of(0, 1);

        Student student = studentRepository.findById(card.getLastAssignedTo())
                .orElseThrow(StudentNotExistException::new);

        if (student.getUserId().getIsDeleted()) {
            throw new UserDeletedException();
        }

        if (!student.getUserId().getIsActivated()) {
            throw new UserNotActivatedException();
        }

        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        List<Attendance> lastAttendanceList = attendanceRepository.getLastAttendanceByStudentId(student.getId(), pageable);
        if (!lastAttendanceList.isEmpty()) {
            Attendance lastAttendance = lastAttendanceList.get(0);
            ZonedDateTime lastArrival = lastAttendance.getArrival();
            ZonedDateTime lastLeaving = lastAttendance.getLeaving();
            if (lastLeaving == null && isSameDay(currDate, lastArrival)) {
                lastAttendance.setLeaving(currDate);
                attendanceRepository.save(lastAttendance);
                System.err.println(cardHash);
                return new ResponseDto(lastAttendance.getId());
            }
        }

        Attendance newAttendance = new Attendance();
        newAttendance.setArrival(currDate);
        newAttendance.setStudentId(student);
        attendanceRepository.save(newAttendance);
        System.err.println(cardHash);
        return new ResponseDto(newAttendance.getId());

    }

    /**
     *
     *
     * @param date1
     * @param date2
     * @return
     */

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
