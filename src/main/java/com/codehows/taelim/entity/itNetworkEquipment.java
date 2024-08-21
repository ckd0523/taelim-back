package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "itNetworkEquipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class itNetworkEquipment {

    @Id
    @Column(name = "networkNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long networkNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String equipmentType;
    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;
    private String serviceScope;
}
