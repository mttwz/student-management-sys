package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkgroupmembersDTO {

    private Integer id;

    private Integer userId;

    private Integer workgroupId;
}
