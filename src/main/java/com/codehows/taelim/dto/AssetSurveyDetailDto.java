package com.codehows.taelim.dto;

import com.codehows.taelim.entity.AssetSurveyDetail;
import com.codehows.taelim.entity.CommonAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssetSurveyDetailDto {
    //타입에 Membmer 사용 불가능
    //일단 Member 타입으로 이 dto를 프론트에 넘겨줬을 때 잘 받을지도 의문
    private String assetCode;
    private String assetName;
    private String assetOwner;
    private String assetSecurityManager;
    private boolean exactLocation;
    private boolean assetStatus;
    private Long infoNo;
    private String assetSurveyContent;

    public AssetSurveyDetailDto(CommonAsset commonAsset, AssetSurveyDetail assetSurveyDetail) {
        this.assetCode = commonAsset.getAssetCode();
        this.assetName = commonAsset.getAssetName();
        this.assetOwner = commonAsset.getAssetOwner().getUName();
        this.assetSecurityManager = commonAsset.getAssetSecurityManager().getUName();
        this.exactLocation = assetSurveyDetail.getExactLocation();
        this.assetStatus = assetSurveyDetail.getAssetStatus();
        this.infoNo = assetSurveyDetail.getInfoNo();
        this.assetSurveyContent = assetSurveyDetail.getAssetSurveyContent();
    }

}
