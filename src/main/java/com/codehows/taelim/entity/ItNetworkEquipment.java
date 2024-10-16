package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itNetworkEquipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItNetworkEquipment {

    @Id
    @Column(name = "networkNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long networkNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String equipmentType;
    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;
    private String serviceScope;
    private String rackUnit;
}
