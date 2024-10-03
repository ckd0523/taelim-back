package com.codehows.taelim.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetUpdateResponse {
    private String message;
    private Long assetNo;

    public AssetUpdateResponse(String message, Long assetNo) {
        this.message = message;
        this.assetNo = assetNo;
    }
}
