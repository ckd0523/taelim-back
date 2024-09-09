package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
//자산 조사 등록할 때 뷰에서 담아주는 것들
public class AssetSurveyHistoryRegisterDto {
    private AssetLocation location;
    private String email;
}
