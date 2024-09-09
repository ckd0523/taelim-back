package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InformationProtectionSystemDto {

    private Long infoNo;

    private CommonAsset assetNo;

    private String serviceScope;
}
