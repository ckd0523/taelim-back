package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
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
}
