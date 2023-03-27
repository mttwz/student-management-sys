package com.radnoti.studentmanagementsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private Integer id;

    private String code;

    public ResponseDto(Integer id) {
        this.id = id;
    }

    public ResponseDto(String code) {
        this.code = code;
    }
}
