package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkgroupScheduleService {

    private final WorkgroupscheduleRepository workgroupscheduleRepository;
    private final WorkgroupRepository workgroupRepository;

    @Transactional
    public Integer createWorkgroupSchedule(WorkgroupscheduleDTO workgroupscheduleDTO) {
        Optional<Workgroup> optionalWorkgroup = workgroupRepository.findById(workgroupscheduleDTO.getWorkgroupId());
        if(optionalWorkgroup.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Workgroup does not exist");
        }

        Integer workgroupScheduleId = workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDTO.getName(), workgroupscheduleDTO.getStart(), workgroupscheduleDTO.getEnd(), workgroupscheduleDTO.getIsOnsite(), workgroupscheduleDTO.getWorkgroupId());
        if (workgroupScheduleId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not created");
        }

        Optional<Workgroupschedule> optionalWorkgroupschedule = workgroupscheduleRepository.findById(workgroupScheduleId);

        if (optionalWorkgroupschedule.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule not created");
        }

        return workgroupScheduleId;
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




