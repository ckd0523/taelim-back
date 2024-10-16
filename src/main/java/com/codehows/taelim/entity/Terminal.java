package com.codehows.taelim.entity;

import com.codehows.taelim.constant.SecurityControl;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "terminal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Terminal {
    @Id
    @Column(name = "terminalNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long terminalNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String ip;
    private String os;

    @Enumerated(EnumType.STRING)
    private SecurityControl securityControl;

    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;

}
