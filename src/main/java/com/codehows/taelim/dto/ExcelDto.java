package com.codehows.taelim.dto;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.InformationProtectionSystem;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelDto {

    private Long assetNo;

    private AssetBasis assetBasis;
    private AssetClassification assetClassification;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private Department department;
    private AssetLocation assetLocation;
    private String assetUser;
    private String assetOwner;
    private String assetSecurityManager;
    private OperationStatus operationStatus;
    private LocalDate introducedDate;
    private int confidentiality;
    private int integrity;
    private int availability;
    private String note;
    private String serviceScope;

    private LocalDate createDate;

    public CommonAsset toExcel() {
        return CommonAsset.builder()
                .assetNo(assetNo)
                .assetClassification(assetClassification)
                .assetCode(assetCode)
                .assetName(assetName)
                .assetBasis(assetBasis)
                .assetLocation(assetLocation)
                .department(department)
                .operationStatus(operationStatus)
//                .assetUser(new Member(assetUser))
//                .assetOwner(new Member(assetOwner))
//                .assetSecurityManager(new Member(assetSecurityManager))
                .purpose(purpose)
                .quantity(quantity)
                .introducedDate(introducedDate)
                .confidentiality(confidentiality)
                .integrity(integrity)
                .availability(availability)
                .note(note)
                .createDate(createDate)
                .build();
    }

    public InformationProtectionSystem toExcelInfo() {
        return InformationProtectionSystem.builder()
                .serviceScope(serviceScope)
                .build();
    }


}
