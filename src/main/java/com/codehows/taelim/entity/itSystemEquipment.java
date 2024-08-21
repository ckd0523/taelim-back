package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "itSystemEquipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class itSystemEquipment {

    @Id
    @Column(name = "equipmentNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String equipmentType;
    private Long rackUnit;
    private String powerSupply;
    private String coolingSystem;
    private String interfacePorts;
    private String formFactor;
    private Long expansionSlots;
    private String graphicsCard;
    private String portConfiguration;
    private Boolean monitorIncluded;
}
