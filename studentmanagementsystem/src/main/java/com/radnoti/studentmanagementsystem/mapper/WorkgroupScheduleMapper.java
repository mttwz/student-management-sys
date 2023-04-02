package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.UserScheduleInfoDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface WorkgroupScheduleMapper {

    @Mapping(source = "workgroupId.id",target = "workgroupId")
    WorkgroupscheduleDto fromEntityToDto(Workgroupschedule workgroupschedule);

    @Mapping(source = "workgroupId",target = "workgroupId.id")
    Workgroupschedule fromDtoToEntity(WorkgroupscheduleDto workgroupscheduleDto);

    @Mapping(source = "workgroupId.id",target = "workgroupId")
    UserScheduleInfoDto fromWorkgroupscheduleToUserScheduleInfoDto(Workgroupschedule workgroupschedule);
}
