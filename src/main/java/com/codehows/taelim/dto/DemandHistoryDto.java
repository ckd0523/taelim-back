package com.codehows.taelim.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemandHistoryDto {

    private Long demandNo;
    private Long assetNo;
    private String assetCode;
    private String demandType;
    private LocalDate demandDate;
    private String demandBy;
    private String demandStatus;

}
