package com.codehows.taelim.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Devices {
    @Id
    @Column(name = "deviceNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;
}
