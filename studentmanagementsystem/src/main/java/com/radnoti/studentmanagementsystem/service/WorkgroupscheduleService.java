package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkgroupscheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;

    private final JwtConfig jwtConfig;

    private final DateFormatUtil dateFormatUtil;
    private final WorkgroupRepository workgroupRepository;


    //    @Transactional
//    public Integer createWorkgroup(WorkgroupDTO workgroupDTO) {
//        Integer workgroupId = workgroupRepository.createWorkgroup(workgroupDTO.getGroupName(), workgroupDTO.getInstitution());
//        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupId);
//        if(optionalWorkgroup.isPresent() && Objects.equals(optionalWorkgroup.get().getGroupName(), workgroupDTO.getGroupName())){
//            return workgroupId;
//        } else throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "NEMJO");
//    }
    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDTO workgroupscheduleDTO) {
        try {
            Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupscheduleDTO.getWorkgroupId());
            Integer workgroupScheduleId = workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDTO.getName(), workgroupscheduleDTO.getStart(), workgroupscheduleDTO.getEnd(), workgroupscheduleDTO.getIsOnsite(), workgroupscheduleDTO.getWorkgroupId());
            Optional<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findById(workgroupScheduleId);
            if(optionalWorkgroupschedule.isPresent() && Objects.equals(optionalWorkgroupschedule.get().getName(), workgroupscheduleDTO.getName())){
                return workgroupScheduleId;
            }else throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not created");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Workgroup does not exist");
        }
    }

//    @Transactional
//    public void uploadFile(MultipartFile file, WorkgroupscheduleDTO workgroupscheduleDTO) {
//
//        try {
//
//
//            byte[] bytes = file.getBytes();
//            System.out.println(bytes.length);
//
//            String rootPath = "/Users/matevoros/Documents/GitHub/student-management-sys/";
//            File dir = new File(rootPath + "uploads");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            File serverFile = new File(dir.getAbsolutePath() + "/" + file.getOriginalFilename());
//            if (serverFile.exists()) {
//                dir.mkdirs();
//            }
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//            stream.write(bytes);
//            stream.close();
//
//
//        } catch (Exception e) {
//
//        }
//
//
//    }
}




