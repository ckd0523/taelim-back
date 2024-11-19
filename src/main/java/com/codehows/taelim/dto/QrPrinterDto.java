package com.codehows.taelim.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QrPrinterDto {
    private Long qrPrinterNo;
    private String printerName;
    private String printerIp;
    private Boolean isSelected;
}
