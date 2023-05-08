package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.attendance.AttendanceNotExistException;
import com.radnoti.studentmanagementsystem.exception.card.CardAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.card.CardMismatchException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.AttendanceMapper;
import com.radnoti.studentmanagementsystem.model.dto.AttendanceDto;
import com.radnoti.studentmanagementsystem.model.dto.PagingDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.UserScheduleInfoDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.DateUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttendanceService {


    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final IdValidatorUtil idValidatorUtil;
    private final CardRepository cardRepository;
    private final AttendanceMapper attendanceMapper;
    private final DateUtil dateUtil;


    /**
     * This method logs the attendance of a student with the given student ID.
     * The method retrieves the student with the given ID from the database, validates the student's status,
     * and creates an attendance record with the current date and time. The method also checks if there is an
     * existing attendance record for the student on the same day, and if so, updates it with the leaving time.
     *
     * @param cardHash ........
     * @return a ResponseDto object containing the ID of the created or updated attendance record
     * @throws InvalidIdException  if the provided student id is invalid eg: if the id contains a string
     * @throws StudentNotExistException  if the student with the given ID does not exist in the database
     * @throws UserDeletedException      if the student's user account has been deleted
     * @throws UserNotActivatedException if the student's user account has not been activated
     */
    @Transactional
    public ResponseDto logStudent(String cardHash){

        Card card = cardRepository.findByHash(cardHash)
                .orElseThrow(CardNotExistException::new);

        if(card.getIsDeleted()){
            throw new CardAlreadyDeletedException();
        }
        if (card.getLastAssignedTo() == null){
            throw new CardNotAssignedException();
        }


        Student student = studentRepository.findById(card.getLastAssignedTo())
                .orElseThrow(StudentNotExistException::new);

        if (student.getCardId() == null || !Objects.equals(student.getCardId().getId(), card.getId())){
            throw new CardMismatchException();
        }


        if (Boolean.TRUE.equals(student.getUserId().getIsDeleted())) {
            throw new UserDeletedException();
        }

        if (Boolean.FALSE.equals(student.getUserId().getIsActivated())) {
            throw new UserNotActivatedException();
        }

        Pageable pageable = PageRequest.of(0, 1);

        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        List<Attendance> lastAttendanceList = attendanceRepository.getLastAttendanceByStudentId(student.getId(), pageable);
        if (!lastAttendanceList.isEmpty()) {
            Attendance lastAttendance = lastAttendanceList.get(0);
            ZonedDateTime lastArrival = lastAttendance.getArrival();
            ZonedDateTime lastLeaving = lastAttendance.getLeaving();
            if (lastLeaving == null && dateUtil.isSameDay(currDate, lastArrival)) {
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
        return new ResponseDto(newAttendance.getId());

    }

    @Transactional
    public void createAttendance(AttendanceDto attendanceDto){
        User user = userRepository.findById(attendanceDto.getUserId())
                .orElseThrow(UserNotExistException::new);


        if (Boolean.TRUE.equals(user.getIsDeleted())){
            throw new UserDeletedException();
        }

        System.err.println(user.getIsActivated());

        if(Boolean.FALSE.equals(user.getIsActivated())){
            throw new UserNotActivatedException();
        }

        Student student = studentRepository.findByUserId(attendanceDto.getUserId())
                .orElseThrow(StudentNotExistException::new);

        attendanceDto.setStudentId(student.getId());
        
        Attendance attendance = attendanceMapper.fromDtoToEntity(attendanceDto);
        attendanceRepository.save(attendance);
    }

    @Transactional
    public PagingDto getAttendanceByUserId(String userIdString,Pageable pageable){
        Integer userId = idValidatorUtil.idValidator(userIdString);
        Page<Attendance> attendancePage = attendanceRepository.getAttendanceByUserId(userId,pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(attendancePage.getTotalPages());
        pagingDto.setAttendanceDtoList(attendancePage.stream().map(attendanceMapper::fromEntityToDto).toList());

        return pagingDto;

    }
    @Transactional
    public PagingDto getAttendanceByStudentId(String studentIdString,Pageable pageable){
        Integer studentId = idValidatorUtil.idValidator(studentIdString);
        Page<Attendance> attendancePage = attendanceRepository.getAttendanceByStudentId(studentId,pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(attendancePage.getTotalPages());
        pagingDto.setAttendanceDtoList(attendancePage.stream().map(attendanceMapper::fromEntityToDto).toList());

        return pagingDto;

    }


    @Transactional
    public List<AttendanceDto> getAttendancePerDayByUserId(UserScheduleInfoDto userScheduleInfoDto, Pageable pageable){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = userScheduleInfoDto.getDate().format(formatter);
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        attendanceRepository.getAttendancePerDayByUserId(userScheduleInfoDto.getUserId(), formattedDate).forEach(attendance -> {
            attendanceDtoList.add(attendanceMapper.fromEntityToDto(attendance));
        });
        return attendanceDtoList;

    }

    @Transactional
    public void editAttendance(AttendanceDto attendanceDto) {
        if(attendanceDto.getId() == null|| attendanceDto.getArrival() == null || attendanceDto.getLeaving() == null ){
            throw new FormValueInvalidException();

        }
        Attendance attendance = attendanceRepository.findById(attendanceDto.getId())
                .orElseThrow(AttendanceNotExistException::new);
        attendance.setArrival(dateUtil.dateConverter(attendanceDto.getArrival()));
        attendance.setLeaving(dateUtil.dateConverter(attendanceDto.getLeaving()));
    }

    public void deleteAttendance(String attendanceIdString) {
        System.err.println(attendanceIdString);
        //lehet csak logikai torles kene idk
        Integer attendanceId = idValidatorUtil.idValidator(attendanceIdString);
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(AttendanceNotExistException::new);

        attendanceRepository.delete(attendance);

    }
}
