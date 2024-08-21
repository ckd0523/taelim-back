package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "electronicInformation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class electronicInformation {

    @Id
    @Column(name = "eInfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eInfoNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String OS;
    private String system;
    private String DBType;
}
