package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    public ArrayList<WorkgroupscheduleDto> getWorkgroupScheduleByUserId(String authHeader, UserDto userDto) {
        ArrayList<Integer> workgroupScheduleList;
        userRepository.findById(userDto.getId())
                .orElseThrow(UserNotExistException::new);

        if (jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN)) {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(userDto.getId());
        } else {
            workgroupScheduleList = userRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromJwt(authHeader));
        }

        ArrayList<WorkgroupscheduleDto> workgroupscheduleDtoArrayList = new ArrayList<>();
        Iterable<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findAllById(workgroupScheduleList);

        for(Workgroupschedule workgroupschedule : optionalWorkgroupschedule){
            workgroupscheduleDtoArrayList.add(workgroupScheduleMapper.fromEntityToDto(workgroupschedule));
        }
        return workgroupscheduleDtoArrayList;
    }


    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) throws ParseException {
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
        workgroupschedule.setStart(new Date(workgroupscheduleDto.getStart().getTime() - 1000 * 3600));
        workgroupschedule.setEnd(new Date(workgroupscheduleDto.getEnd().getTime() - 1000 * 3600));
        workgroupschedule.setIsOnsite(workgroupschedule.getIsOnsite());


        System.err.println();

        Workgroupschedule savedWorkgroupSchedule = workgroupscheduleRepository.save(workgroupschedule);

        return savedWorkgroupSchedule.getId();

    }




    @Transactional
    public Integer deleteWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) {
        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupScheduleNotExistException::new);

        workgroupscheduleRepository.delete(workgroupschedule);
        return workgroupscheduleDto.getId();

    }






    public void uploadFile(String pathVariableWorkgroupScheduleId, MultipartFile file) {
//        Integer workgroupScheduleId;
//        try{
//            workgroupScheduleId = Integer.parseInt(pathVariableWorkgroupScheduleId);
//        } catch (NumberFormatException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid id");
//        }
//
//        Workgroupschedule workgroupschedule = existanceCheckUtil.workgroupScheduleExistenceCheck(workgroupScheduleId);
//
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime now = LocalDateTime.now();
//
//        byte[] bytes;
//        try {
//            bytes = file.getBytes();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String rootPath = "/Users/matevoros/Desktop";
//        File dir = new File(rootPath + "/uploads/" + workgroupschedule.getName() + "_" + workgroupschedule + dateTimeFormatter.format(now));
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File serverFile = new File(dir.getAbsolutePath() + "/" + file.getOriginalFilename());
//        if (serverFile.exists()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "File already exist");
//        }
//
//        try {
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//            stream.write(bytes);
//            stream.close();
//        } catch (IOException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Something went wrong");
//        }


    }
}




