package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "software")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Software {

    @Id
    @Column(name = "softwareNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long softwareNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String IP;
    private String serverId;
    private String serverPassword;
    private String companyManager;
    private String OS;

}
