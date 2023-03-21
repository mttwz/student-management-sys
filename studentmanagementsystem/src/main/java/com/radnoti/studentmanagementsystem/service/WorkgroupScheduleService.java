package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;



@Service
@RequiredArgsConstructor
public class WorkgroupScheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;
    private final WorkgroupRepository workgroupRepository;

    private final UserRepository userRepository;

    private final WorkgroupScheduleMapper workgroupScheduleMapper;
    private final JwtUtil jwtUtil;


    @Transactional
    public List<WorkgroupscheduleDto> getWorkgroupScheduleByUserId(String authHeader, UserDto userDto) {

        List<Integer> workgroupScheduleList;
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        if (jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDto.getId());
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromJwt(authHeader));
        }

        List<WorkgroupscheduleDto> workgroupscheduleDtoList = new ArrayList<>();
        Iterable<Workgroupschedule> optionalWorkgroupSchedule = workgroupscheduleRepository.findAllById(workgroupScheduleList);

        for(Workgroupschedule workgroupschedule : optionalWorkgroupSchedule){
            WorkgroupscheduleDto workgroupscheduleDto = workgroupScheduleMapper.fromEntityToDto(workgroupschedule);
            workgroupscheduleDtoList.add(workgroupscheduleDto);
        }
        return workgroupscheduleDtoList;
    }


    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto){
// TODO: 2023. 03. 19. datumok szarok kikell javitni
        if(workgroupscheduleDto.getName() == null ||
                workgroupscheduleDto.getWorkgroupId() == null ||
                workgroupscheduleDto.getStart() == null ||
                workgroupscheduleDto.getEnd() == null ||
                workgroupscheduleDto.getIsOnsite() == null ||
                workgroupscheduleDto.getName().isEmpty()){
            throw new InvalidFormValueException();
        }

        workgroupRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        Workgroupschedule workgroupschedule = workgroupScheduleMapper.fromDtoToEntity(workgroupscheduleDto);

        workgroupschedule.setName(workgroupscheduleDto.getName());
        workgroupschedule.setWorkgroupId(new Workgroup(workgroupscheduleDto.getWorkgroupId()));
        workgroupschedule.setStart(workgroupscheduleDto.getStart());
        workgroupschedule.setEnd(workgroupscheduleDto.getEnd());
        workgroupschedule.setIsOnsite(workgroupschedule.getIsOnsite());
        workgroupschedule.setIsDeleted(false);


        Workgroupschedule savedWorkgroupSchedule = workgroupscheduleRepository.save(workgroupschedule);

        return savedWorkgroupSchedule.getId();

    }




    @Transactional
    public void deleteWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) {
        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupscheduleDto.getId())
                .orElseThrow(WorkgroupScheduleNotExistException::new);
        if (workgroupschedule.getIsDeleted()){
            throw new WorkgroupAlreadyDeletedException();
        }
        ZonedDateTime currDate = java.time.ZonedDateTime.now();
        workgroupschedule.setIsDeleted(true);
        workgroupschedule.setDeletedAt(currDate);

    }

    @Transactional
    public void restoreDeletedWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) {
        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupscheduleDto.getId())
                .orElseThrow(WorkgroupScheduleNotExistException::new);
        workgroupschedule.setIsDeleted(false);
        workgroupschedule.setDeletedAt(null);

    }

}




