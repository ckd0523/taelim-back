package com.codehows.taelim.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RepairHistoryDto {

    private Long repairNo;
    private Long assetNo;
    private LocalDate repairStartDate;
    private LocalDate repairEnDate;
    private String repairBy;
    private String repairResult;

    private List<RepairFileDto> repairFileDtos; // 리스트로 변경
}