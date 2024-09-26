package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Software;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareDto {
    private Long softwareNo;

    private CommonAsset assetNo;

    private String ip;
    private String serverId;
    private String serverPassword;
    private String companyManager;
    private String os;


    // DTO에서 엔티티로 변환
    public Software toEntity() {
        return Software.builder()
                .softwareNo(this.softwareNo)
                .assetNo(this.assetNo)
                .ip(this.ip)
                .serverId(this.serverId)
                .serverPassword(this.serverPassword)
                .companyManager(this.companyManager)
                .os(this.os)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static SoftwareDto fromEntity(Software entity) {
        return new SoftwareDto(
                entity.getSoftwareNo(),
                entity.getAssetNo(),
                entity.getIp(),
                entity.getServerId(),
                entity.getServerPassword(),
                entity.getCompanyManager(),
                entity.getOs()
        );
    }
}
