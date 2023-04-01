package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.enums.RoleEnum;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import com.radnoti.studentmanagementsystem.exception.workgroupSchedule.WorkgroupScheduleNotExistException;
import com.radnoti.studentmanagementsystem.mapper.WorkgroupScheduleMapper;
import com.radnoti.studentmanagementsystem.model.dto.PagingDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupRepository;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import com.radnoti.studentmanagementsystem.security.JwtUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
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


    @Transactional
    public PagingDto getWorkgroupScheduleByUserId(String authHeader, String userIdString, Pageable pageable) {

        Integer userId = idValidatorUtil.idValidator(userIdString);

        Page<Workgroupschedule> workgroupSchedulePage;
        PagingDto pagingDto = new PagingDto();

        userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.SUPERADMIN) || jwtUtil.getRoleFromJwt(authHeader).equalsIgnoreCase(RoleEnum.Types.ADMIN)) {
            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByUserId(userId,pageable);
        } else {
            workgroupSchedulePage = workgroupscheduleRepository.getWorkgroupScheduleByUserId(jwtUtil.getIdFromJwt(authHeader),pageable);
        }


        pagingDto.setWorkgroupscheduleDtoList(workgroupSchedulePage.stream().map(workgroupScheduleMapper::fromEntityToDto).toList());
        pagingDto.setAllPages(workgroupSchedulePage.getTotalPages());


        return pagingDto;
    }

    @Transactional
    public List<WorkgroupscheduleDto> getWorkgroupScheduleByWorkgroupId(String workgroupIdString, Pageable pageable) {
        Integer workgroupId = idValidatorUtil.idValidator(workgroupIdString);

        Workgroup workgroup = workgroupRepository.findById(workgroupId)
                .orElseThrow(WorkgroupNotExistException::new);

        Page<Workgroupschedule> workgroupschedulePage = workgroupscheduleRepository.getWorkgroupScheduleByWorkgroupId(workgroup.getId(),pageable);

        return workgroupschedulePage.stream().map(workgroupScheduleMapper::fromEntityToDto).toList();
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
        workgroupschedule.setStart(workgroupscheduleDto.getStart());
        workgroupschedule.setEnd(workgroupscheduleDto.getEnd());
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

}




