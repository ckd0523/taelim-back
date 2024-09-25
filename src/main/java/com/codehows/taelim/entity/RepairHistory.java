package com.codehows.taelim.entity;

import com.codehows.taelim.dto.RepairDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "repairHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RepairHistory {
    @Id
    @Column(name = "repairNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private LocalDate repairStartDate;
    private LocalDate repairEndDate;
    private String repairBy;
    private String repairResult;
    private Boolean repairStatus;

    public RepairDto toDto(){
        return RepairDto.builder()
                .repairNo(repairNo)
                .assetCode(assetNo.getAssetCode())
                .assetName(assetNo.getAssetName())
                .repairResult(repairResult)
                .repairStartDate(repairStartDate)
                .repairEndDate(repairEndDate)
        .build();
    }

}
