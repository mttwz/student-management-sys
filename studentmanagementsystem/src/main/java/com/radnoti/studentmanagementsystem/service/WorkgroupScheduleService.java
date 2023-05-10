package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.AttendanceMapper;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.AttendanceRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import com.radnoti.studentmanagementsystem.util.DateUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;




@Service
@RequiredArgsConstructor
public class WorkgroupScheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;
    private final WorkgroupRepository workgroupRepository;

    private final UserRepository userRepository;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;
    private final JwtUtil jwtUtil;

    private final IdValidatorUtil idValidatorUtil;
    private final DateUtil dateUtil;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;


    @Transactional
    public PagingDto getWorkgroupScheduleByUserId(String authHeader, String userIdString, Pageable pageable) {

        Integer userId = idValidatorUtil.idValidator(userIdString);

        Page<Workgroupschedule> workgroupSchedulePage;
        PagingDto pagingDto = new PagingDto();

        userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (jwtUtil.getRoleFromAuthHeader(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN) || jwtUtil.getRoleFromAuthHeader(authHeader).equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByUserId(userId,pageable);
        } else {
            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromAuthHeader(authHeader),pageable);
        }


        pagingDto.setWorkgroupscheduleDtoList(workgroupSchedulePage.stream().map(workgroupScheduleMapper::fromEntityToDto).toList());
        pagingDto.setAllPages(workgroupSchedulePage.getTotalPages());


        return pagingDto;
    }

    @Transactional
    public PagingDto getWorkgroupScheduleByWorkgroupId(String authHeader, WorkgroupscheduleDto workgroupscheduleDto, Pageable pageable) {


        Page<Workgroupschedule> workgroupSchedulePage;
        PagingDto pagingDto = new PagingDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = workgroupscheduleDto.getStart().format(formatter);

        workgroupRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        if (jwtUtil.getRoleFromAuthHeader(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN) || jwtUtil.getRoleFromAuthHeader(authHeader).equalsIgnoreCase(RoleEnum.Types.ADMIN)) {

            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByWorkgroupId(workgroupscheduleDto.getWorkgroupId(),formattedDate,pageable);
        } else {
            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByWorkgroupId(jwtUtil.getIdFromAuthHeader(authHeader),formattedDate,pageable);
        }


        pagingDto.setWorkgroupscheduleDtoList(workgroupSchedulePage.stream().map(workgroupScheduleMapper::fromEntityToDto).toList());
        pagingDto.setAllPages(workgroupSchedulePage.getTotalPages());

        return pagingDto;

    }


    @Transactional
    public ResponseDto createWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto){

        if(workgroupscheduleDto.getName() == null ||
                workgroupscheduleDto.getWorkgroupId() == null ||
                workgroupscheduleDto.getStart() == null ||
                workgroupscheduleDto.getEnd() == null ||
                workgroupscheduleDto.getIsOnsite() == null ||
                workgroupscheduleDto.getName().isEmpty()){
            throw new FormValueInvalidException();
        }

        workgroupRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        Workgroupschedule workgroupschedule = workgroupScheduleMapper.fromDtoToEntity(workgroupscheduleDto);

        workgroupschedule.setName(workgroupscheduleDto.getName());
        workgroupschedule.setWorkgroupId(new Workgroup(workgroupscheduleDto.getWorkgroupId()));
        workgroupschedule.setStart(dateUtil.dateConverter(workgroupscheduleDto.getStart()));
        workgroupschedule.setEnd(dateUtil.dateConverter(workgroupscheduleDto.getEnd()));
        workgroupschedule.setIsOnsite(workgroupschedule.getIsOnsite());
        workgroupschedule.setIsDeleted(false);

        Workgroupschedule savedWorkgroupSchedule = workgroupscheduleRepository.save(workgroupschedule);

        return new ResponseDto(savedWorkgroupSchedule.getId());

    }
    @Transactional
    public void deleteWorkgroupSchedule(String workgroupScheduleIdString) {

        Integer workgroupScheduleId = idValidatorUtil.idValidator(workgroupScheduleIdString);

        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupScheduleId)
                .orElseThrow(WorkgroupScheduleNotExistException::new);
        if (workgroupschedule.getIsDeleted()){
            throw new WorkgroupAlreadyDeletedException();
        }

        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        workgroupschedule.setIsDeleted(true);
        workgroupschedule.setDeletedAt(currDate);

    }

    @Transactional
    public void restoreDeletedWorkgroupSchedule(String workgroupScheduleIdString) {

        Integer workgroupScheduleId = idValidatorUtil.idValidator(workgroupScheduleIdString);

        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupScheduleId)
                .orElseThrow(WorkgroupScheduleNotExistException::new);

        workgroupschedule.setIsDeleted(false);
        workgroupschedule.setDeletedAt(null);

    }
    @Transactional
    public List<UserScheduleInfoDto> getUserSchedule(UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {
        if(userScheduleInfoDto.getUserId() == null || userScheduleInfoDto.getDate() == null){
            throw new FormValueInvalidException();
        }
        User user =  userRepository.findById(userScheduleInfoDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = userScheduleInfoDto.getDate().format(formatter);
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        attendanceRepository.getAttendancePerDayByUserId(user.getId(),formattedDate).forEach(attendance -> {
            attendanceDtoList.add(attendanceMapper.fromEntityToDto(attendance));
        });

        Page<Workgroupschedule> workgroupschedulePage = workgroupscheduleRepository.getWorkgroupSchedulePerDayByUserId(user.getId(),formattedDate,pageable);
        List<UserScheduleInfoDto> userScheduleInfoDtoList = new ArrayList<>();

        List<WorkgroupscheduleDto> workgroupscheduleDtoList = new ArrayList<>();



        workgroupschedulePage.forEach(workgroupschedule -> {
            UserScheduleInfoDto currentUserScheduleInfo = workgroupScheduleMapper.fromWorkgroupscheduleToUserScheduleInfoDto(workgroupschedule);
            WorkgroupscheduleDto workgroupscheduleDto = workgroupScheduleMapper.fromEntityToDto(workgroupschedule);
            workgroupscheduleDtoList.add(workgroupscheduleDto);
            currentUserScheduleInfo.setUserId(user.getId());
            currentUserScheduleInfo.setDate(userScheduleInfoDto.getDate());
            currentUserScheduleInfo.setIsStudentPresent(false);
            userScheduleInfoDtoList.add(currentUserScheduleInfo);




        });


        calculateLate(attendanceDtoList,workgroupscheduleDtoList,userScheduleInfoDtoList);

        return userScheduleInfoDtoList;
    }

    @Transactional
    public PagingDto getOwnUserSchedule(String authHeader, UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {
        Integer userId = jwtUtil.getIdFromAuthHeader(authHeader);
        if(userScheduleInfoDto.getDate() == null){
            throw new FormValueInvalidException();
        }
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = userScheduleInfoDto.getDate().format(formatter);
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        attendanceRepository.getAttendancePerDayByUserId(user.getId(),formattedDate).forEach(attendance -> {
            attendanceDtoList.add(attendanceMapper.fromEntityToDto(attendance));
        });

        Page<Workgroupschedule> workgroupschedulePage = workgroupscheduleRepository.getWorkgroupSchedulePerDayByUserId(user.getId(),formattedDate,pageable);

        List<UserScheduleInfoDto> userScheduleInfoDtoList = new ArrayList<>();

        List<WorkgroupscheduleDto> workgroupscheduleDtoList = new ArrayList<>();

        PagingDto pagingDto = new PagingDto();


        workgroupschedulePage.forEach(workgroupschedule -> {
            UserScheduleInfoDto currentUserScheduleInfo = workgroupScheduleMapper.fromWorkgroupscheduleToUserScheduleInfoDto(workgroupschedule);
            WorkgroupscheduleDto workgroupscheduleDto = workgroupScheduleMapper.fromEntityToDto(workgroupschedule);
            workgroupscheduleDtoList.add(workgroupscheduleDto);
            currentUserScheduleInfo.setUserId(user.getId());
            currentUserScheduleInfo.setDate(userScheduleInfoDto.getDate());
            currentUserScheduleInfo.setIsStudentPresent(false);
            userScheduleInfoDtoList.add(currentUserScheduleInfo);




        });


        calculateLate(attendanceDtoList,workgroupscheduleDtoList,userScheduleInfoDtoList);

        pagingDto.setAllPages(workgroupschedulePage.getTotalPages());
        pagingDto.setUserScheduleInfoDtoList(userScheduleInfoDtoList);
        return pagingDto;
    }

    public List<UserScheduleInfoDto> gerUserScheduleInWorkgroup(UserScheduleInfoDto userScheduleInfoDto, Pageable pageable) {
        if(userScheduleInfoDto.getUserId() == null || userScheduleInfoDto.getDate() == null || userScheduleInfoDto.getWorkgroupId() == null){
            throw new FormValueInvalidException();
        }

        User user =  userRepository.findById(userScheduleInfoDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        Workgroup workgroup =  workgroupRepository.findById(userScheduleInfoDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = userScheduleInfoDto.getDate().format(formatter);

        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        attendanceRepository.getAttendancePerDayByUserId(user.getId(),formattedDate).forEach(attendance -> {
            attendanceDtoList.add(attendanceMapper.fromEntityToDto(attendance));
        });

        Page<Workgroupschedule> workgroupschedulePage = workgroupscheduleRepository.getWorkgroupSchedulePerDayPerWgByUserId(user.getId(), workgroup.getId(), formattedDate,pageable);
        List<UserScheduleInfoDto> userScheduleInfoDtoList = new ArrayList<>();

        List<WorkgroupscheduleDto> workgroupscheduleDtoList = new ArrayList<>();



        workgroupschedulePage.forEach(workgroupschedule -> {
            UserScheduleInfoDto currentUserScheduleInfo = workgroupScheduleMapper.fromWorkgroupscheduleToUserScheduleInfoDto(workgroupschedule);
            WorkgroupscheduleDto workgroupscheduleDto = workgroupScheduleMapper.fromEntityToDto(workgroupschedule);
            workgroupscheduleDtoList.add(workgroupscheduleDto);
            currentUserScheduleInfo.setUserId(user.getId());
            currentUserScheduleInfo.setDate(userScheduleInfoDto.getDate());
            currentUserScheduleInfo.setIsStudentPresent(false);
            userScheduleInfoDtoList.add(currentUserScheduleInfo);




        });
        calculateLate(attendanceDtoList,workgroupscheduleDtoList,userScheduleInfoDtoList);

        return userScheduleInfoDtoList;
    }


    public void calculateLate(List<AttendanceDto> attendanceDtoList, List<WorkgroupscheduleDto> workgroupscheduleDtoList, List<UserScheduleInfoDto> userScheduleInfoDtoList){
        for (int i = 0; i < workgroupscheduleDtoList.size(); i++) {
            ZonedDateTime scheduleStart = workgroupscheduleDtoList.get(i).getStart();
            ZonedDateTime scheduleEnd = workgroupscheduleDtoList.get(i).getEnd();
            Long scheduleLengthInMinute = Duration.between(scheduleStart,scheduleEnd).toMinutes();
            Long stayInDur = 0L;

            if(attendanceDtoList.isEmpty()){
                userScheduleInfoDtoList.get(i).setLateInMinutes(scheduleLengthInMinute);
            }



            for (int j = 0; j < attendanceDtoList.size(); j++) {
                ZonedDateTime checkIn = attendanceDtoList.get(j).getArrival();
                ZonedDateTime checkOut = null;
                if(attendanceDtoList.get(j).getLeaving() == null){
                    checkOut = checkIn.withHour(23).withMinute(59).withSecond(59);
                }else checkOut = attendanceDtoList.get(j).getLeaving();

                System.err.println(workgroupscheduleDtoList.get(i).getName());
                System.err.println(scheduleStart);
                System.err.println(scheduleEnd);
                System.err.println(checkIn);
                System.err.println(checkOut);

                //overlap check
                if (((scheduleStart.isBefore(checkOut) || scheduleStart.isEqual(checkOut)) && (scheduleEnd.isAfter(checkIn) || scheduleEnd.isEqual(checkIn)))) {
                    //whole time
                    if ((checkIn.isBefore(scheduleStart) || checkIn.isEqual(scheduleStart)) && (checkOut.isAfter(scheduleEnd) || checkOut.isEqual(scheduleEnd))) {
                        stayInDur += scheduleLengthInMinute;
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);

                    }
                    // left early
                    else if ((checkIn.isBefore(scheduleStart) || checkIn.isEqual(scheduleStart)) && checkOut.isBefore(scheduleEnd)) {
                        stayInDur += Duration.between(scheduleStart, checkOut).toMinutes();
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);

                    }
                    // arrive late
                    else if ( (checkIn.isAfter(scheduleStart)) && (checkOut.isEqual(scheduleEnd) || checkOut.isAfter(scheduleEnd))) {
                        stayInDur += Duration.between(checkIn, scheduleEnd).toMinutes();
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);

                    }
                    //arrive late left early
                    else if (!userScheduleInfoDtoList.get(i).getIsStudentPresent() && (checkIn.isAfter(scheduleStart) && checkOut.isBefore(scheduleEnd))) {
                        stayInDur += Duration.between(checkIn, checkOut).toMinutes();
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);

                    }
                    //arrive late left early AGAIN
                    else if (userScheduleInfoDtoList.get(i).getIsStudentPresent() && (checkIn.isAfter(scheduleStart) && checkOut.isBefore(scheduleEnd))) {
                        stayInDur += Duration.between(checkIn, checkOut).toMinutes();
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);
                    }
                    //came back finally
                    else if (userScheduleInfoDtoList.get(i).getIsStudentPresent() && (checkIn.isAfter(scheduleStart) && checkOut.isAfter(scheduleEnd))) {
                        stayInDur += Duration.between(checkIn, scheduleEnd).toMinutes();
                        setLate(userScheduleInfoDtoList.get(i),scheduleLengthInMinute,stayInDur);
                    }
                }
                //no overlap but student should be there
                else if (!userScheduleInfoDtoList.get(i).getIsStudentPresent()){
                    userScheduleInfoDtoList.get(i).setLateInMinutes(scheduleLengthInMinute);


                }


            }


        }
    }

    void setLate(UserScheduleInfoDto userScheduleInfoDto,Long scheduleLengthInMinute, Long stayInDur){
        userScheduleInfoDto.setLateInMinutes(scheduleLengthInMinute-stayInDur);
        userScheduleInfoDto.setIsStudentPresent(true);
    }



}




