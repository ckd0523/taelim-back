package com.codehows.taelim.entity;

import com.codehows.taelim.constant.securityControl;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "terminal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class terminal {
    @Id
    @Column(name = "terminalNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long terminalNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String IP;
    private String productSerialNumber;
    private String OS;
    private securityControl securityControl;
    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;

}
