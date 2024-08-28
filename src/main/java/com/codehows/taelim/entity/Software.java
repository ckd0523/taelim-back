package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "software")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Software {

    @Id
    @Column(name = "softwareNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long softwareNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String ip;
    private String serverId;
    private String serverPassword;
    private String companyManager;
    private String os;

}
