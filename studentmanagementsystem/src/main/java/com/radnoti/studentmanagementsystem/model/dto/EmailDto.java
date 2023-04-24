package com.radnoti.studentmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmailDto {
    private String to;
    private String subject;
    private String text;


}
