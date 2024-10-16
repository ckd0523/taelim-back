package com.codehows.taelim.dto;

import com.codehows.taelim.constant.SecurityControl;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Terminal;
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
    private String os;

    private SecurityControl securityControl;

    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;

    // DTO에서 엔티티로 변환
    public Terminal toEntity() {
        return Terminal.builder()
                .terminalNo(this.terminalNo)
                .assetNo(this.assetNo)
                .ip(this.ip)
                .os(this.os)
                .securityControl(this.securityControl)
                .kaitsKeeper(this.kaitsKeeper)
                .V3OfficeSecurity(this.V3OfficeSecurity)
                .appCheckPro(this.appCheckPro)
                .tgate(this.tgate)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static TerminalDto fromEntity(Terminal entity) {
        return new TerminalDto(
                entity.getTerminalNo(),
                entity.getAssetNo(),
                entity.getIp(),
                entity.getOs(),
                entity.getSecurityControl(),
                entity.getKaitsKeeper(),
                entity.getV3OfficeSecurity(),
                entity.getAppCheckPro(),
                entity.getTgate()
        );
    }
}
