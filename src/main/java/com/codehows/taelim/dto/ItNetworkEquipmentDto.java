package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ItNetworkEquipment;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItNetworkEquipmentDto {
    private Long networkNo;

    private CommonAsset assetNo;

    private String equipmentType;
    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;
    private String serviceScope;

    // DTO에서 엔티티로 변환
    public ItNetworkEquipment toEntity() {
        return ItNetworkEquipment.builder()
                .networkNo(this.networkNo)
                .assetNo(this.assetNo)
                .equipmentType(this.equipmentType)
                .numberOfPorts(this.numberOfPorts)
                .supportedProtocols(this.supportedProtocols)
                .firmwareVersion(this.firmwareVersion)
                .networkSpeed(this.networkSpeed)
                .serviceScope(this.serviceScope)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static ItNetworkEquipmentDto fromEntity(ItNetworkEquipment entity) {
        return new ItNetworkEquipmentDto(
                entity.getNetworkNo(),
                entity.getAssetNo(),
                entity.getEquipmentType(),
                entity.getNumberOfPorts(),
                entity.getSupportedProtocols(),
                entity.getFirmwareVersion(),
                entity.getNetworkSpeed(),
                entity.getServiceScope()
        );
    }
}
