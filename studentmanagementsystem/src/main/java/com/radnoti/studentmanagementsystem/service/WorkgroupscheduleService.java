package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.util.DateFormatUtil;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class WorkgroupscheduleService {
    @Autowired
    private WorkgroupscheduleRepository workgroupscheduleRepository;

    JwtUtil jwtUtil = new JwtUtil();
    DateFormatUtil dateFormatUtil = new DateFormatUtil();

    @Transactional
    public void createWorkgroupSchedule(String jwt, WorkgroupscheduleDTO workgroupscheduleDTO){
            if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
                workgroupscheduleRepository.createWorkgroupSchedule(workgroupscheduleDTO.getName(),workgroupscheduleDTO.getWorkgroupId(),workgroupscheduleDTO.getStart(),workgroupscheduleDTO.getEnd(),workgroupscheduleDTO.getOnsite());

            }
    }
//teszt


}
