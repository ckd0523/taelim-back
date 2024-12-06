package com.codehows.taelim.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
//분류별 각 자산 개수
public class AssetClassificationAmountDto {

    private long informationProtectionSystemAmount;
    private long applicationProgramAmount;
    private long softwareAmount;
    private long electronicInformationAmount;
    private long documentAmount;
    private long patentsAndTrademarksAmount;
    private long itSystemEquipAmount;
    private long itNetworkEquipAmount;
    private long terminalAmount;
    private long furnitureAmount;
    private long devicesAmount;
    private long carAmount;
    private long otherAssetsAmount;
}
