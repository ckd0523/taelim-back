package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "electronicInformation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ElectronicInformation {

    @Id
    @Column(name = "eInfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eInfoNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String os;
    private String system;
    private String dbtype;
}
