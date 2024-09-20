package com.codehows.taelim.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateHistoryDto {

    private Long updateNo;
    private Long assetNo;
    private String assetCode;
    private String assetName;
    private String updateReason;
    private String updateDetail;
    private String updateBy;
    private LocalDate updateDate;

}
