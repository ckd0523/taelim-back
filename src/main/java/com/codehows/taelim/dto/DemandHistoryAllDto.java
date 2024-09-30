package com.codehows.taelim.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemandHistoryAllDto {
    private String assetCode;
    private Long assetNo;
    private String demandBy;
    private LocalDate demandDate;
    private Long demandNo;
    private String demandStatus;
    private String demandType;
}
