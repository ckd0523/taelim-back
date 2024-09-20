package com.codehows.taelim.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MaintainDto {

    private Long maintainNo;
    private String assetCode;
    private String assetName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String maintainBy;
    private String maintainContent;

}
