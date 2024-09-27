package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.OtherAssets;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtherAssetsDto {
    private Long otherNo;

    private CommonAsset assetNo;

    private String otherDescription;
    private String usageFrequency;

    // DTO에서 엔티티로 변환
    public OtherAssets toEntity() {
        return OtherAssets.builder()
                .otherNo(this.otherNo)
                .assetNo(this.assetNo)
                .otherDescription(this.otherDescription)
                .usageFrequency(this.usageFrequency)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static OtherAssetsDto fromEntity(OtherAssets entity) {
        return new OtherAssetsDto(
                entity.getOtherNo(),
                entity.getAssetNo(),
                entity.getOtherDescription(),
                entity.getUsageFrequency()
        );
    }
}
