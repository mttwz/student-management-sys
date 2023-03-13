package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDTO;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface WorkgroupscheduleMapper {

    @Mapping(source = "workgroupId.id",target = "workgroupId")
    WorkgroupscheduleDTO fromEntityToDto(Workgroupschedule workgroupschedule);
}
