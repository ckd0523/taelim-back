package com.codehows.taelim.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RepairHistoryDto {

    private Long repairNo;
    private Long assetNo;
    private LocalDate repairStartDate;
    private LocalDate repairEnDDate;
    private String repairBy;
    private String repairResult;

    private RepairFileDto repairFileDto;
}
