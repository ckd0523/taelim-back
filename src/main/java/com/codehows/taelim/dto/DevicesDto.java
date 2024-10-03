package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Devices;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DevicesDto {
    private Long deviceNo;

    private CommonAsset assetNo;

    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;

    // DTO에서 엔티티로 변환
    public Devices toEntity() {
        return Devices.builder()
                .deviceNo(this.deviceNo)
                .assetNo(this.assetNo)
                .deviceType(this.deviceType)
                .modelNumber(this.modelNumber)
                .connectionType(this.connectionType)
                .powerSpecifications(this.powerSpecifications)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static DevicesDto fromEntity(Devices entity) {
        return new DevicesDto(
                entity.getDeviceNo(),
                entity.getAssetNo(),
                entity.getDeviceType(),
                entity.getModelNumber(),
                entity.getConnectionType(),
                entity.getPowerSpecifications()
        );
    }
}
