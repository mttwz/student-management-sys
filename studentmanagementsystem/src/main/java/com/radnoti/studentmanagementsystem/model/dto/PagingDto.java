package com.radnoti.studentmanagementsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import com.radnoti.studentmanagementsystem.repository.WorkgroupscheduleRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PagingDto{
    private Integer allPages;
    private List<UserInfoDto> userInfoDtoList;
    private List<WorkgroupscheduleDto> workgroupscheduleDtoList;
    private List<WorkgroupDto> workgroupDtoList;
    private List<CardDto> cardDtoList;
    private List<AttendanceDto> attendanceDtoList;

}
