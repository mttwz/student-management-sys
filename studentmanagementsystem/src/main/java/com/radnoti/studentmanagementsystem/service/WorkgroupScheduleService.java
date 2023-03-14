package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkgroupScheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;
    private final WorkgroupRepository workgroupRepository;

    public Workgroupschedule workgroupscheduleExistanceCheck(Integer workgroupscheduleId){
        Optional<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findById(workgroupscheduleId);
        if(optionalWorkgroupschedule.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not exist");
        }
        return optionalWorkgroupschedule.get();
    }


    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDTO workgroupscheduleDTO) {
        if(workgroupscheduleDTO.getName() == null || workgroupscheduleDTO.getWorkgroupId() == null || workgroupscheduleDTO.getStart() == null || workgroupscheduleDTO.getEnd() == null || workgroupscheduleDTO.getIsOnsite() == null){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is null");
        }

        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupscheduleDTO.getWorkgroupId());
        if (optionalWorkgroup.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Workgroup does not exist");
        }

        if(workgroupscheduleDTO.getName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Form value is empty");
        }


        Integer workgroupScheduleId = workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDTO.getName(), workgroupscheduleDTO.getStart(), workgroupscheduleDTO.getEnd(), workgroupscheduleDTO.getIsOnsite(), workgroupscheduleDTO.getWorkgroupId());

        if (workgroupScheduleId == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not created");
        }

        return workgroupScheduleId;

    }



    @Transactional
    public Integer deleteWorkgroupSchedule(WorkgroupscheduleDTO workgroupscheduleDTO) {
        Workgroupschedule workgroupschedule = workgroupscheduleExistanceCheck(workgroupscheduleDTO.getId());
        workgroupscheduleRepository.delete(workgroupschedule);
        if(workgroupscheduleRepository.existsById(workgroupscheduleDTO.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not deleted");
        }
        return workgroupscheduleDTO.getId();

    }






    public void uploadFile(String pathVariableWorkgroupScheduleId, MultipartFile file) {
        Integer workgroupScheduleId;
        try{
            workgroupScheduleId = Integer.parseInt(pathVariableWorkgroupScheduleId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid id");
        }

        Workgroupschedule workgroupschedule = workgroupscheduleExistanceCheck(workgroupScheduleId);


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String rootPath = "/Users/matevoros/Desktop";
        File dir = new File(rootPath + "/uploads/" + workgroupschedule.getName() + "_" + workgroupschedule + dateTimeFormatter.format(now));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File serverFile = new File(dir.getAbsolutePath() + "/" + file.getOriginalFilename());
        if (serverFile.exists()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "File already exist");
        }

        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Something went wrong");
        }


    }
}




