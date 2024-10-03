package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.InformationProtectionSystem;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InformationProtectionSystemDto {

    private Long infoNo;

    private CommonAsset assetNo;

    private String serviceScope;

    // DTO에서 엔티티로 변환
    public InformationProtectionSystem toEntity() {
        return InformationProtectionSystem.builder()
                .infoNo(this.infoNo)
                .assetNo(this.assetNo)
                .serviceScope(this.serviceScope)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static InformationProtectionSystemDto fromEntity(InformationProtectionSystem entity) {
        return new InformationProtectionSystemDto(
                entity.getInfoNo(),
                entity.getAssetNo(),
                entity.getServiceScope()
        );
    }
}
