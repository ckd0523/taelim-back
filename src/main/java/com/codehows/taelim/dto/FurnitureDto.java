package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Furniture;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureDto {
    private Long furnitureNo;

    private CommonAsset assetNo;

    private String furnitureSize;

    // DTO에서 엔티티로 변환
    public Furniture toEntity() {
        return Furniture.builder()
                .furnitureNo(this.furnitureNo)
                .assetNo(this.assetNo)
                .furnitureSize(this.furnitureSize)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static FurnitureDto fromEntity(Furniture entity) {
        return new FurnitureDto(
                entity.getFurnitureNo(),
                entity.getAssetNo(),
                entity.getFurnitureSize()
        );
    }
}
