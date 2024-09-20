package com.codehows.taelim.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeleteHistoryDto {

    private Long deleteNo;
    private String assetCode;
    private String assetName;
    private String deleteReason;
    private String deleteDetail;
    private String deleteMethod;
    private String deleteLocation;
    private String deleteBy;
    private LocalDate deleteDate;
}
