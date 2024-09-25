package com.codehows.taelim.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemandHistoryDto {



    private String assetCode;
    private Long assetNo;
    private String demandBy;
    private LocalDate demandDate;
    private Long demandNo;
    private String demandStatus;
    private String demandType;

}
