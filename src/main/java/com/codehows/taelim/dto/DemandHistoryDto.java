package com.codehows.taelim.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemandHistoryDto {



    private String assetCode;
    private Long assetNo;
    private List<Long> assetNos;
    private List<String> assetCodes;
    private String demandBy;
    private LocalDate demandDate;
    private Long demandNo;
    private String demandStatus;
    private String demandType;

}
