package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itSystemEquipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItSystemEquipment {

    @Id
    @Column(name = "equipmentNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String equipmentType;
    private String powerSupply;
    private String coolingSystem;
    private String interfacePorts;
    private String formFactor;
    private Long expansionSlots;
    private String graphicsCard;
    private String portConfiguration;
    private Boolean monitorIncluded;
}
