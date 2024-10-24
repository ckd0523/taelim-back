package com.codehows.taelim.dto;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonAssetDto {

    private Long assetNo;
    private AssetClassification assetClassification;
    private AssetBasis assetBasis;
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
    private String manufacturingCompany;
    private Ownership ownership;
    private Long purchaseCost;
    private LocalDate purchaseDate;
    private Long usefulLife;
    private DepreciationMethod depreciationMethod;
    private String warrantyDetails;
    private String attachment;
    private String purchaseSource;
    private String contactInformation;
    private Boolean disposalStatus;
    private Boolean demandStatus; // requestStatus 아님
    private Approval approval;
    private Boolean demandCheck;
    private LocalDate createDate;
    private UseStated useStated;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

    // DTO에서 엔티티로 변환
    public CommonAsset toEntity(CommonAssetDto commonAssetDto) {
        return CommonAsset.builder()
                .assetNo(this.assetNo)
                .assetClassification(this.assetClassification)
                .assetBasis(this.assetBasis)
                .assetCode(this.assetCode)
                .assetName(this.assetName)
                .purpose(this.purpose)
                .quantity(this.quantity)
                .department(this.department)
                .assetLocation(this.assetLocation)
                .assetUser(assetUser)
                .assetOwner(assetOwner)
                .assetSecurityManager(assetSecurityManager)
                .operationStatus(this.operationStatus)
                .introducedDate(this.introducedDate)
                .confidentiality(this.confidentiality)
                .integrity(this.integrity)
                .availability(this.availability)
                .note(this.note)
                .manufacturingCompany(this.manufacturingCompany)
                .ownership(this.ownership)
                .purchaseCost(this.purchaseCost)
                .purchaseDate(this.purchaseDate)
                .usefulLife(this.usefulLife)
                .depreciationMethod(this.depreciationMethod)
                .warrantyDetails(this.warrantyDetails)
                .attachment(this.attachment)
                .purchaseSource(this.purchaseSource)
                .contactInformation(this.contactInformation)
                .disposalStatus(this.disposalStatus)
                .demandStatus(this.demandStatus)
                .approval(this.approval)
                .demandCheck(this.demandCheck)
                .createDate(this.createDate)
                .useStated(this.useStated)
                .acquisitionRoute(this.acquisitionRoute)
                .maintenancePeriod(this.maintenancePeriod)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static CommonAssetDto fromEntity(CommonAsset entity) {
        return new CommonAssetDto(
                entity.getAssetNo(),
                entity.getAssetClassification(),
                entity.getAssetBasis(),
                entity.getAssetCode(),
                entity.getAssetName(),
                entity.getPurpose(),
                entity.getQuantity(),
                entity.getDepartment(),
                entity.getAssetLocation(),
                entity.getAssetUser() != null ? entity.getAssetUser() : null,
                entity.getAssetOwner() != null ? entity.getAssetOwner() : null,
                entity.getAssetSecurityManager() != null ? entity.getAssetSecurityManager() : null,
                entity.getOperationStatus(),
                entity.getIntroducedDate(),
                entity.getConfidentiality(),
                entity.getIntegrity(),
                entity.getAvailability(),
                entity.getNote(),
                entity.getManufacturingCompany(),
                entity.getOwnership(),
                entity.getPurchaseCost(),
                entity.getPurchaseDate(),
                entity.getUsefulLife(),
                entity.getDepreciationMethod(),
                entity.getWarrantyDetails(),
                entity.getAttachment(),
                entity.getPurchaseSource(),
                entity.getContactInformation(),
                entity.getDisposalStatus(),
                entity.getDemandStatus(),
                entity.getApproval(),
                entity.getDemandCheck(),
                entity.getCreateDate(),
                entity.getUseStated(),
                entity.getAcquisitionRoute(),
                entity.getMaintenancePeriod()
        );
    }

}
