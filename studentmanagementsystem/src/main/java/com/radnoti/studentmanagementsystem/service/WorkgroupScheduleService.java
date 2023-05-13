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




    /**
     * Retrieves the workgroup schedule for a user based on the provided user ID and authentication header.
     * The method first validates the user ID to ensure it exists in the database, throwing a UserNotExistException if not.
     * It then checks the role specified in the authentication header to determine if the user is a superadmin or admin.
     * If so, it retrieves the workgroup schedule for the specified user ID. Otherwise, it retrieves the schedule for the user specified in the authentication header.
     * The method returns a PagingDto object containing the workgroup schedule results and pagination information.
     *
     * @param authHeader   a string representing the authentication header
     * @param userIdString a string representing the ID of the user
     * @param pageable     a Pageable object defining the pagination parameters
     * @return a PagingDto object containing the workgroup schedule results and pagination information
     * @throws UserNotExistException if the user with the specified ID does not exist
     */
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



    /**
     * Retrieves the workgroup schedule for a specific workgroup based on the provided parameters and authentication header.
     * The method first validates the workgroup ID to ensure it exists in the database, throwing a WorkgroupNotExistException if not.
     * It then checks the role specified in the authentication header to determine if the user is a superadmin or admin.
     * If so, it retrieves the workgroup schedule for the specified workgroup ID and start date.
     * Otherwise, it retrieves the schedule for the user specified in the authentication header and the specified start date.
     * The method returns a PagingDto object containing the workgroup schedule results and pagination information.
     *
     * @param authHeader          a string representing the authentication header
     * @param workgroupscheduleDto a WorkgroupscheduleDto object containing the workgroup ID and start date
     * @param pageable            a Pageable object defining the pagination parameters
     * @return a PagingDto object containing the workgroup schedule results and pagination information
     * @throws WorkgroupNotExistException if the workgroup with the specified ID does not exist
     */
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


    /**
     * Creates a new workgroup schedule based on the provided WorkgroupscheduleDto object.
     * The method first validates the required fields in the WorkgroupscheduleDto object, throwing a FormValueInvalidException if any field is missing or empty.
     * It then validates the existence of the workgroup specified in the WorkgroupscheduleDto object, throwing a WorkgroupNotExistException if the workgroup does not exist.
     * The method maps the WorkgroupscheduleDto object to a Workgroupschedule entity and sets its properties accordingly.
     * The created workgroup schedule is saved to the database, and the ID of the saved workgroup schedule is returned in a ResponseDto object.
     *
     * @param workgroupscheduleDto a WorkgroupscheduleDto object containing the details of the workgroup schedule
     * @return a ResponseDto object containing the ID of the created workgroup schedule
     * @throws FormValueInvalidException   if a required field is missing or empty in the WorkgroupscheduleDto object
     * @throws WorkgroupNotExistException  if the workgroup with the specified ID does not exist
     */
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


    /**
     * Deletes a workgroup schedule with the specified ID.
     * The method first validates the ID of the workgroup schedule using the idValidatorUtil.
     * It then retrieves the workgroup schedule from the database based on the ID, throwing a WorkgroupScheduleNotExistException if it does not exist.
     * If the workgroup schedule is already deleted, a WorkgroupAlreadyDeletedException is thrown.
     * The method sets the isDeleted flag of the workgroup schedule to true and sets the deletedAt timestamp to the current date and time.
     *
     * @param workgroupScheduleIdString the ID of the workgroup schedule to be deleted
     * @throws WorkgroupScheduleNotExistException if the workgroup schedule with the specified ID does not exist
     * @throws WorkgroupAlreadyDeletedException  if the workgroup schedule is already deleted
     */
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


    /**
     * Restores a deleted workgroup schedule with the specified ID.
     * The method first validates the ID of the workgroup schedule using the idValidatorUtil.
     * It then retrieves the workgroup schedule from the database based on the ID, throwing a WorkgroupScheduleNotExistException if it does not exist.
     * The method sets the isDeleted flag of the workgroup schedule to false and sets the deletedAt timestamp to null, effectively restoring the workgroup schedule.
     *
     * @param workgroupScheduleIdString the ID of the workgroup schedule to be restored
     * @throws WorkgroupScheduleNotExistException if the workgroup schedule with the specified ID does not exist
     */
    @Transactional
    public void restoreDeletedWorkgroupSchedule(String workgroupScheduleIdString) {

        Integer workgroupScheduleId = idValidatorUtil.idValidator(workgroupScheduleIdString);

        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupScheduleId)
                .orElseThrow(WorkgroupScheduleNotExistException::new);

        workgroupschedule.setIsDeleted(false);
        workgroupschedule.setDeletedAt(null);

    }


    /**
     * Retrieves the schedule information for a user on a specific date.
     * The method first validates the user ID and date in the userScheduleInfoDto.
     * It then retrieves the user from the database based on the user ID, throwing a UserNotExistException if the user does not exist.
     * The method formats the date in the userScheduleInfoDto and retrieves the attendance records for the user on that date.
     * It also retrieves the workgroup schedules for the user on that date, using pagination.
     * The method calculates whether the user is present for each workgroup schedule and sets the corresponding flags in the UserScheduleInfoDto objects.
     * The method returns a list of UserScheduleInfoDto objects containing the schedule information for the user.
     *
     * @param userScheduleInfoDto the user schedule information containing the user ID and date
     * @param pageable the pagination information for retrieving the workgroup schedules
     * @return a list of UserScheduleInfoDto objects representing the schedule information for the user
     * @throws FormValueInvalidException if the user ID or date in the userScheduleInfoDto is null
     * @throws UserNotExistException if the user with the specified ID does not exist
     */
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



    /**
     * Retrieves the schedule information for the authenticated user on a specific date.
     * The method first obtains the user ID from the authentication header using the jwtUtil.
     * It then validates the date in the userScheduleInfoDto, throwing a FormValueInvalidException if it is null.
     * The method retrieves the authenticated user from the database based on the user ID, throwing a UserNotExistException if the user does not exist.
     * The method formats the date in the userScheduleInfoDto and retrieves the attendance records for the user on that date.
     * It also retrieves the workgroup schedules for the user on that date, using pagination.
     * The method calculates whether the user is present for each workgroup schedule and sets the corresponding flags in the UserScheduleInfoDto objects.
     * The method returns a PagingDto object containing the schedule information for the user and pagination details.
     *
     * @param authHeader the authentication header containing the JWT token
     * @param userScheduleInfoDto the user schedule information containing the date
     * @param pageable the pagination information for retrieving the workgroup schedules
     * @return a PagingDto object containing the schedule information for the user and pagination details
     * @throws FormValueInvalidException if the date in the userScheduleInfoDto is null
     * @throws UserNotExistException if the authenticated user does not exist
     */
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



    /**
     * Retrieves the schedule information for a specific user in a workgroup on a given date.
     * The method validates the user ID, date, and workgroup ID in the userScheduleInfoDto,
     * throwing a FormValueInvalidException if any of these values is null.
     * The method retrieves the user and workgroup from the database based on the IDs provided,
     * throwing UserNotExistException or WorkgroupNotExistException if the corresponding entities do not exist.
     * The method formats the date in the userScheduleInfoDto and retrieves the attendance records for the user on that date.
     * It also retrieves the workgroup schedules for the user in the specified workgroup on that date, using pagination.
     * The method calculates whether the user is present for each workgroup schedule and sets the corresponding flags in the UserScheduleInfoDto objects.
     * The method returns a list of UserScheduleInfoDto objects containing the schedule information for the user in the workgroup.
     *
     * @param userScheduleInfoDto the user schedule information containing the user ID, date, and workgroup ID
     * @param pageable the pagination information for retrieving the workgroup schedules
     * @return a list of UserScheduleInfoDto objects containing the schedule information for the user in the workgroup
     * @throws FormValueInvalidException if the user ID, date, or workgroup ID in the userScheduleInfoDto is null
     * @throws UserNotExistException if the specified user does not exist
     * @throws WorkgroupNotExistException if the specified workgroup does not exist
     */
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


    /**
     * Calculates the lateness for each user's attendance in relation to the scheduled workgroup events.
     *
     * @param attendanceDtoList       The list of attendance records for the user.
     * @param workgroupscheduleDtoList The list of workgroup schedules for the user.
     * @param userScheduleInfoDtoList  The list of user schedule information DTOs.
     */
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


    /**
     * Sets the lateness information in the UserScheduleInfoDto object.
     *
     * @param userScheduleInfoDto   The UserScheduleInfoDto object to update.
     * @param scheduleLengthInMinute The length of the workgroup schedule in minutes.
     * @param stayInDur             The duration the user stayed within the workgroup schedule in minutes.
     */
    void setLate(UserScheduleInfoDto userScheduleInfoDto,Long scheduleLengthInMinute, Long stayInDur){
        userScheduleInfoDto.setLateInMinutes(scheduleLengthInMinute-stayInDur);
        userScheduleInfoDto.setIsStudentPresent(true);
    }



}




