package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applicationProgram")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationProgram {
    @Id
    @Column(name = "appNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String serviceScope;
    private String os;
    private String relatedDB;
    private String ip;
    private Long screenNumber;
}
