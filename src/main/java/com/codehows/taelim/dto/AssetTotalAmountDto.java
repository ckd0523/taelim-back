package com.codehows.taelim.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetTotalAmountDto {
    private long repairAmount; //유지보수 건 수
    private long assetSurveyAmount; //자산조사 건 수
    private long totalAssetAmount; //총 자산 개수
    //private long totalPurchaseCost; //자산 총액은 소유 자산 + 임대 자산으로 계산 가능
    private long ownCost; //소유 자산 총액
    private long leasedCost; //임대 자산 총액

//    public AssetTotalAmountDto(long repairAmount, long assetSurveyAmount, long totalAssetAmount, long ownCost, long leasedCost) {
//        this.repairAmount = repairAmount;
//        this.assetSurveyAmount = assetSurveyAmount;
//        this.totalAssetAmount = totalAssetAmount;
//        this.ownCost = ownCost;
//        this.leasedCost = leasedCost;
//    }
}
