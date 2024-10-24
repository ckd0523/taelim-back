package com.codehows.taelim.entity;

import com.codehows.taelim.constant.RepairStatus;
import com.codehows.taelim.dto.RepairDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private String repairStatus;

    // kwon 추가 - repairFile 불러오기위해
    @OneToMany(mappedBy = "repairNo", fetch = FetchType.LAZY)
    private List<RepairFile> repairFiles; // RepairFile 리스트 추가

    public RepairDto toDto(){
        return RepairDto.builder()
                .repairNo(repairNo)
                .assetNo(assetNo.getAssetNo())
                .assetCode(assetNo.getAssetCode())
                .assetName(assetNo.getAssetName())
                .repairBy(repairBy)
                .repairResult(repairResult)
                .repairStartDate(repairStartDate)
                .repairEndDate(repairEndDate)
                .repairStatus(repairStatus)
        .build();
    }

}
