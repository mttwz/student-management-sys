package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

@Service
public class WorkgroupscheduleService {
    @Autowired
    private WorkgroupscheduleRepository workgroupscheduleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DateFormatUtil dateFormatUtil;

    @Transactional
    public void createWorkgroupSchedule(WorkgroupscheduleDTO workgroupscheduleDTO) {
        workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDTO.getName(), workgroupscheduleDTO.getWorkgroupId(), workgroupscheduleDTO.getStart(), workgroupscheduleDTO.getEnd(), workgroupscheduleDTO.getOnsite());
    }

    @Transactional
    public void uploadFile(MultipartFile file, WorkgroupscheduleDTO workgroupscheduleDTO) {

        try {


            byte[] bytes = file.getBytes();
            System.out.println(bytes.length);

            String rootPath = "/Users/matevoros/Documents/GitHub/student-management-sys/";
            File dir = new File(rootPath + "uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            File serverFile = new File(dir.getAbsolutePath() + "/" + file.getOriginalFilename());
            if (serverFile.exists()) {
                dir.mkdirs();
            }
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            System.out.println("Server File Location=" + serverFile.getAbsolutePath());
        } catch (Exception e) {

        }


    }
}
//teszt



