package com.codehows.taelim.dto;

import com.codehows.taelim.entity.DemandDtl;
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
    private String demandBy;
    private LocalDate demandDate;
    private Long demandNo;
    private String demandStatus;
    private String demandType;
    private List<DemandHistoryAllDto> subRows;

}
