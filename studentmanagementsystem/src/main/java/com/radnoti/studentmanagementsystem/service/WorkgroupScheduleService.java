package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.EmptyFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class WorkgroupScheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;
    private final WorkgroupRepository workgroupRepository;



    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) {
        if(workgroupscheduleDto.getName() == null || workgroupscheduleDto.getWorkgroupId() == null || workgroupscheduleDto.getStart() == null || workgroupscheduleDto.getEnd() == null || workgroupscheduleDto.getIsOnsite() == null){
            throw new NullFormValueException();
        }
        workgroupRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupNotExistException::new);

        if(workgroupscheduleDto.getName().isEmpty()){
            throw new EmptyFormValueException();
        }

        Integer workgroupScheduleId = workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDto.getName(), workgroupscheduleDto.getStart(), workgroupscheduleDto.getEnd(), workgroupscheduleDto.getIsOnsite(), workgroupscheduleDto.getWorkgroupId());

        workgroupscheduleRepository.findById(workgroupScheduleId)
                .orElseThrow(WorkgroupScheduleNotCreatedException::new);

        return workgroupScheduleId;

    }


    @Transactional
    public Integer deleteWorkgroupSchedule(WorkgroupscheduleDto workgroupscheduleDto) {
        Workgroupschedule workgroupschedule = workgroupscheduleRepository.findById(workgroupscheduleDto.getWorkgroupId())
                .orElseThrow(WorkgroupScheduleNotExistException::new);

        workgroupscheduleRepository.delete(workgroupschedule);

        if(workgroupscheduleRepository.existsById(workgroupscheduleDto.getId())){
            throw new WorkgroupScheduleNotDeletedException();
        }
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




