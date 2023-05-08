package com.radnoti.studentmanagementsystem.mapper;

import com.radnoti.studentmanagementsystem.model.dto.AttendanceDto;
import com.radnoti.studentmanagementsystem.model.entity.Attendance;
import com.radnoti.studentmanagementsystem.util.DateUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = DateUtil.class
)
public interface AttendanceMapper {

    @Mapping(source = "studentId.id",target = "studentId")
    AttendanceDto fromEntityToDto(Attendance attendance);

    @Mapping(target = "arrival", expression = "java(new DateUtil().dateConverter(attendanceDto.getArrival()))")
    @Mapping(target = "leaving", expression = "java(new DateUtil().dateConverter(attendanceDto.getLeaving()))")

    @Mapping(source = "studentId",target = "studentId.id")
    Attendance fromDtoToEntity(AttendanceDto attendanceDto);

}
