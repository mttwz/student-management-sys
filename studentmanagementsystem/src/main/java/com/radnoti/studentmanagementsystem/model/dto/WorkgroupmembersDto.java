package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkgroupmembersDto {

    private Integer id;

    private Integer userId;

    private Integer workgroupId;
}
