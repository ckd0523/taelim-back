package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ElectronicInformation;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicInformationDto {
    private Long eInfoNo;

    private CommonAsset assetNo;

    private String os;
    private String system;
    private String DBType;

    // DTO에서 엔티티로 변환
    public ElectronicInformation toEntity() {
        return ElectronicInformation.builder()
                .eInfoNo(this.eInfoNo)
                .assetNo(this.assetNo)
                .os(this.os)
                .system(this.system)
                .dbtype(this.DBType)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static ElectronicInformationDto fromEntity(ElectronicInformation entity) {
        return new ElectronicInformationDto(
                entity.getEInfoNo(),
                entity.getAssetNo(),
                entity.getOs(),
                entity.getSystem(),
                entity.getDbtype()
        );
    }
}
