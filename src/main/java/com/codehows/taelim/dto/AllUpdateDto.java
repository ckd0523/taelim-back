package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllUpdateDto {
    private List<AssetUpdateDto> assetDtos;
    private AssetUpdateDto assetDto;
    private Long assetNo;
    private String department;
    private String assetLocation;
    private String assetOwner;
    private String assetUser;
    private String assetSecurityManager;
    private String demandBy;
    private String reason;
    private String detail;
}
