package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.attendance.AttendanceNotExistException;
import com.radnoti.studentmanagementsystem.exception.card.CardAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.card.CardMismatchException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
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
import com.radnoti.studentmanagementsystem.security.JwtUtil;
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
    private final JwtUtil jwtUtil;


    /**
     * Logs the attendance of a student using the provided card hash.
     *
     * @param cardHash The hash value of the card used for attendance logging.
     * @return A ResponseDto object containing the ID of the attendance record.
     * @throws CardNotExistException If the card with the given hash does not exist.
     * @throws CardAlreadyDeletedException If the card has already been deleted.
     * @throws CardNotAssignedException If the card has not been assigned to any student.
     * @throws StudentNotExistException If the student associated with the card does not exist.
     * @throws CardMismatchException If there is a mismatch between the card and the student.
     * @throws UserDeletedException If the user associated with the student has been deleted.
     * @throws UserNotActivatedException If the user associated with the student has not been activated.
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


    /**
     * Creates an attendance record based on the provided AttendanceDto.
     *
     * @param attendanceDto the AttendanceDto object containing the attendance information
     * @throws UserNotExistException if the user with the provided ID does not exist
     * @throws UserDeletedException if the user is marked as deleted
     * @throws UserNotActivatedException if the user is not activated
     * @throws StudentNotExistException if the student corresponding to the user does not exist
     */
    @Transactional
    public void createAttendance(AttendanceDto attendanceDto){
        User user = userRepository.findById(attendanceDto.getUserId())
                .orElseThrow(UserNotExistException::new);


        if (Boolean.TRUE.equals(user.getIsDeleted())){
            throw new UserDeletedException();
        }

        if(Boolean.FALSE.equals(user.getIsActivated())){
            throw new UserNotActivatedException();
        }

        Student student = studentRepository.findByUserId(attendanceDto.getUserId())
                .orElseThrow(StudentNotExistException::new);

        attendanceDto.setStudentId(student.getId());
        
        Attendance attendance = attendanceMapper.fromDtoToEntity(attendanceDto);
        attendanceRepository.save(attendance);
    }


    /**
     * Retrieves a paginated list of attendance records for the user with the provided user ID.
     *
     * @param userIdString the ID of the user as a string
     * @param pageable the Pageable object specifying the page size and number
     * @return a PagingDto object containing the paginated list of attendance records
     * @throws IllegalArgumentException if the provided user ID is invalid
     */
    @Transactional
    public PagingDto getAttendanceByUserId(String userIdString,Pageable pageable){
        Integer userId = idValidatorUtil.idValidator(userIdString);
        Page<Attendance> attendancePage = attendanceRepository.getAttendanceByUserId(userId,pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(attendancePage.getTotalPages());
        pagingDto.setAttendanceDtoList(attendancePage.stream().map(attendanceMapper::fromEntityToDto).toList());

        return pagingDto;

    }



    /**
     * Retrieves a paginated list of attendance records for the student with the provided student ID.
     *
     * @param studentIdString the ID of the student as a string
     * @param pageable the Pageable object specifying the page size and number
     * @return a PagingDto object containing the paginated list of attendance records
     * @throws IllegalArgumentException if the provided student ID is invalid
     */
    @Transactional
    public PagingDto getAttendanceByStudentId(String studentIdString,Pageable pageable){
        Integer studentId = idValidatorUtil.idValidator(studentIdString);
        Page<Attendance> attendancePage = attendanceRepository.getAttendanceByStudentId(studentId,pageable);
        PagingDto pagingDto = new PagingDto();
        pagingDto.setAllPages(attendancePage.getTotalPages());
        pagingDto.setAttendanceDtoList(attendancePage.stream().map(attendanceMapper::fromEntityToDto).toList());

        return pagingDto;

    }


    /**
     * Retrieves a list of attendance records per day for the user with the provided user ID,
     * based on the specified date in the UserScheduleInfoDto object.
     *
     * @param userScheduleInfoDto the UserScheduleInfoDto object containing the user ID and date
     * @param pageable the Pageable object specifying the page size and number
     * @return a list of AttendanceDto objects representing the attendance records per day
     * @throws IllegalArgumentException if the provided user ID is invalid
     */
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


    /**
     * Edits the attendance record based on the provided AttendanceDto.
     *
     * @param attendanceDto the AttendanceDto object containing the updated attendance information
     * @throws FormValueInvalidException if the ID, arrival, or leaving value in the AttendanceDto is null
     * @throws AttendanceNotExistException if the attendance record with the provided ID does not exist
     */
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


    /**
     * Deletes the attendance record with the provided attendance ID.
     *
     * @param attendanceIdString the ID of the attendance record as a string
     * @throws IllegalArgumentException if the provided attendance ID is invalid
     * @throws AttendanceNotExistException if the attendance record with the provided ID does not exist
     */
    public void deleteAttendance(String attendanceIdString) {
        System.err.println(attendanceIdString);
        //lehet csak logikai torles kene idk
        Integer attendanceId = idValidatorUtil.idValidator(attendanceIdString);
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(AttendanceNotExistException::new);

        attendanceRepository.delete(attendance);

    }

    public PagingDto getOwnAttendancePerDay(String authHeader, UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {

        Integer userId = jwtUtil.getIdFromAuthHeader(authHeader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = userScheduleInfoDto.getDate().format(formatter);
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        Page<Attendance> attendanceDtoPage = attendanceRepository.getOwnAttendancePerDay(userId, formattedDate,pageable);
        PagingDto pagingDto = new PagingDto();

        attendanceRepository.getOwnAttendancePerDay(userId, formattedDate,pageable).forEach(attendance -> {
            attendanceDtoList.add(attendanceMapper.fromEntityToDto(attendance));
        });

        pagingDto.setAllPages(attendanceDtoPage.getTotalPages());
        pagingDto.setAttendanceDtoList(attendanceDtoList);
        return pagingDto;
    }
}
