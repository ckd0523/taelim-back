package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllUpdateDto {
    private AssetUpdateDto assetDto;
    private Long assetNo;
    private String department;
    private String assetLocation;
    private String reason;
    private String detail;
}
