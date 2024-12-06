package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "qrPrinter")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QRPrinter {
    @Id
    @Column(name = "qrPrinterNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrPrinterNo;
    private String printerName;
    private String printerIp;
    private Boolean isSelected;
}
