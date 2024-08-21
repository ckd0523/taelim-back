package com.codehows.taelim.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class devices {
    @Id
    @Column(name = "deviceNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;
}
