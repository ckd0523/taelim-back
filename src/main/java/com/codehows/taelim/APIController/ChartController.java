package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetTotalAmountDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chart")
public class ChartController {

    private final ChartService chartService;

    //헤더에 표시될 데이터
    @GetMapping("/1")
    public AssetTotalAmountDto getAssetTotalAmount() {
        AssetTotalAmountDto assetTotalAmountDto = new AssetTotalAmountDto();

        Long totalOwnedPurchaseCost = chartService.getTotalOwnedPurchaseCost(); //소유 자산 총액 가져오기
        Long totalLeasedPurchaseCost = chartService.getTotalLeasedPurchaseCost(); //임대 자산 총액 가져오기
        Long repairingAmount = chartService.getRepairingAmount(); //유지 보수 중인 레코드 가져오기
        Long surveyAmount = chartService.getSurveyAmount(); //자산 조사 중인 레코드 가져오기

        assetTotalAmountDto.setOwnCost(totalOwnedPurchaseCost);
        assetTotalAmountDto.setLeasedCost(totalLeasedPurchaseCost);
        assetTotalAmountDto.setRepairAmount(repairingAmount);
        assetTotalAmountDto.setAssetSurveyAmount(surveyAmount);

        return assetTotalAmountDto;
    }
}
