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
public class DevicesDto {
    private Long deviceNo;

    private CommonAsset assetNo;

    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;
}
