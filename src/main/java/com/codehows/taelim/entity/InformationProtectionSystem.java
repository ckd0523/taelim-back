package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "informationProtectionSystem")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InformationProtectionSystem {

    @Id
    @Column(name = "InfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String serviceScope;

}
