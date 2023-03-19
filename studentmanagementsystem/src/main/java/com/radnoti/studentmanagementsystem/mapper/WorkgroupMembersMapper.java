package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupmembersDto;
import com.radnoti.studentmanagementsystem.model.dto.WorkgroupscheduleDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupmembers;
import com.radnoti.studentmanagementsystem.model.entity.Workgroupschedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface WorkgroupMembersMapper {

    @Mapping(source = "userId",target = "userId.id")
    @Mapping(source = "workgroupId",target = "workgroupId.id")
    Workgroupmembers fromDtoToEntity(WorkgroupmembersDto workgroupmembersDto);
}
