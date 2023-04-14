package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupDto;

import com.radnoti.studentmanagementsystem.model.dto.WorkgroupInfoDto;
import com.radnoti.studentmanagementsystem.model.entity.Workgroup;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface WorkgroupMapper {


    WorkgroupDto fromEntityToDto(Workgroup workgroup);

    WorkgroupInfoDto fromEntityToInfoDto(Workgroup workgroup);


    Workgroup fromDtoToEntity(WorkgroupDto workgroupDto);
}
