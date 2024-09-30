package com.codehows.taelim.dto;


import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class RepairDto {

    private Long repairNo;
    private Long assetNo;
    private String assetCode;
    private String assetName;
    private LocalDate repairStartDate;
    private LocalDate repairEndDate;
    private String repairBy;
    private String repairResult;
    private List<RepairFileDto> repairFiles;


    public CommonAsset toAssetNo() {
        return CommonAsset.builder().assetNo(assetNo).build();
    }
    public RepairHistory toRepairHistory() {
        return RepairHistory.builder()
                .repairNo(repairNo)
                .assetNo(toAssetNo())
                .repairStartDate(repairStartDate)
                .repairEndDate(repairEndDate)
                .repairBy(repairBy)
                .repairResult(repairResult)
                .build();
    }



}
