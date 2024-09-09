package com.codehows.taelim.dto;

import com.codehows.taelim.constant.SecurityControl;
import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TerminalDto {
    private Long terminalNo;

    private CommonAsset assetNo;

    private String ip;
    private String productSerialNumber;
    private String os;

    private SecurityControl securityControl;

    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;
}
