package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
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
}
