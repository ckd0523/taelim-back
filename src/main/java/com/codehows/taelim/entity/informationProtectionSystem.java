package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "informationProtectionSystem")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class informationProtectionSystem {

    @Id
    @Column(name = "InfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String serviceScope;

}
