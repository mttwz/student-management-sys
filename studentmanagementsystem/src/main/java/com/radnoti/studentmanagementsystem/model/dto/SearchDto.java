package com.radnoti.studentmanagementsystem.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchDto {
    private String searchText;

    private String searchFilter;

}
