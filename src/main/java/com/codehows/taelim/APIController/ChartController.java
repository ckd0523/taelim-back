package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.constant.OperationStatus;
import com.codehows.taelim.constant.Ownership;
import com.codehows.taelim.dto.AssetClassificationAmountDto;
import com.codehows.taelim.dto.AssetTotalAmountDto;
import com.codehows.taelim.dto.ByDepartmentAmountDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        Long totalAssetAmount = chartService.getTotalAssetAmount(); //자산 총 개수 가져오기

        assetTotalAmountDto.setOwnCost(totalOwnedPurchaseCost);
        assetTotalAmountDto.setLeasedCost(totalLeasedPurchaseCost);
        assetTotalAmountDto.setRepairAmount(repairingAmount);
        assetTotalAmountDto.setAssetSurveyAmount(surveyAmount);
        assetTotalAmountDto.setTotalAssetAmount(totalAssetAmount);

        return assetTotalAmountDto;
    }

    @GetMapping("/2")
    public AssetClassificationAmountDto getAssetClassificationAmount() {
        return chartService.getAssetClassificationAmount();

    }

    @GetMapping("/3")
    public ByDepartmentAmountDto getByDepartmentAmount() {
        return chartService.getByDepartmentAmount();
    }

    @GetMapping("/4")
    public Map<Department, Map<AssetClassification, Long>> getDepartmentClassificationAmount() {
        return chartService.getDepartmentAssetClassificationAmount();
    }

    @GetMapping ("/5")
    public Map<OperationStatus, Long> getOperationAmount() {
        return chartService.getOperationAmount();
    }

    @GetMapping("/6")
    public Map<Ownership, Long> getOwnershipAmount() {
        return chartService.getOwnershipAmount();
    }

    @GetMapping("/7")
    public Map<Integer, Long> getPurchaseCost() {
        return chartService.getPurchaseCost();
    }

    @GetMapping("/8")
    public Map<String, Long> getAssetGrades() {
        return chartService.getAssetGrades();
    }

    @GetMapping("/9/{referenceDate}")
    public ResponseEntity<Map<AssetClassification, Long>> getAssetEndOfLife(
            @RequestParam(value = "referenceDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate referenceDate
            ) {
        if (referenceDate == null) {
            referenceDate = LocalDate.of(2024, 12, 1); // Default date for testing
        }
        Map<AssetClassification, Long> assetsNearEndOfLife = chartService.getAssetNearEndOfLifeCount(referenceDate);
        return ResponseEntity.ok(assetsNearEndOfLife);
    }


}
