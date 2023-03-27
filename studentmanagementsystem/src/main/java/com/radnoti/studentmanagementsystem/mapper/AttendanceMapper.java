package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.AttendanceDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface AttendanceMapper {

    AttendanceDto fromEntityToDto(Attendance attendance);

}
