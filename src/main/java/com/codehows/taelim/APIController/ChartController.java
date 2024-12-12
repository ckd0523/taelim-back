package com.codehows.taelim.APIController;

import com.beust.ah.A;
import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.AssetClassificationAmountDto;
import com.codehows.taelim.dto.AssetTotalAmountDto;
import com.codehows.taelim.dto.ByDepartmentAmountDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
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

        // null일 경우 0으로 설정
        assetTotalAmountDto.setOwnCost(totalOwnedPurchaseCost != null ? totalOwnedPurchaseCost : 0L);
        assetTotalAmountDto.setLeasedCost(totalLeasedPurchaseCost != null ? totalLeasedPurchaseCost : 0L);
        assetTotalAmountDto.setRepairAmount(repairingAmount != null ? repairingAmount : 0L);
        assetTotalAmountDto.setAssetSurveyAmount(surveyAmount != null ? surveyAmount : 0L);
        assetTotalAmountDto.setTotalAssetAmount(totalAssetAmount != null ? totalAssetAmount : 0L);

        return assetTotalAmountDto;
    }

    //분류별 자산 비율
    @GetMapping("/2")
    public Map<AssetClassification, Long> getAssetClassificationAmount() {
        return chartService.getAssetClassificationAmount();

    }

    //부서별 자산 현황
    @GetMapping("/3")
    public Map<Department, Long> getByDepartmentAmount() {
        return chartService.getByDepartmentAmount();
    }

    //부서별 자산 현황2(각 부서의 자산 종류까지)
    @GetMapping("/4")
    public Map<Department, Map<AssetClassification, Long>> getDepartmentClassificationAmount() {
        return chartService.getDepartmentAssetClassificationAmount();
    }

    //운용 현황
    @GetMapping ("/5")
//    public Map<OperationStatus, Long> getOperationAmount() {
//        return chartService.getOperationAmount();
//    }

    public Map<OperationStatus, Long> getOperationAmount() {
        return chartService.getOperationStatusAmount();
    }

    //소유권별 현황
    @GetMapping("/6")
    public Map<Ownership, Long> getOwnershipAmount() {
        return chartService.getOwnershipAmount();
    }

    //자산 총액 추이
    @GetMapping("/7")
    public Map<Integer, Long> getPurchaseCost(@RequestParam("year") int year) {
        System.out.println("Received year : " + year);
        return chartService.getPurchaseCost(year);
    }

    //중요성별 현황
    @GetMapping("/8")
    public Map<String, Long> getAssetGrades() {
        return chartService.getAssetGrades();
    }

    //폐기에정
    @GetMapping("/9")
    public ResponseEntity<Map<AssetClassification, Long>> getAssetEndOfLife(){

        Map<AssetClassification, Long> assetsNearEndOfLife = chartService.getAssetNearEndOfLifeCount();
        return ResponseEntity.ok(assetsNearEndOfLife);
    }

    //위치별 자산 개수
    @GetMapping("/10/{assetLocation}")
    public ResponseEntity<?> getAssetsAssetLocation(@PathVariable("assetLocation") String assetLocationStr) {

        System.out.println("Available AssetLocation values: " + Arrays.toString(AssetLocation.values()));

        try {
            // Convert the string to AssetLocation enum
            AssetLocation assetLocation = AssetLocation.valueOf(assetLocationStr);

            // Call the service to get the asset count data
           Map<AssetClassification, Long> assetLocationData = chartService.getAssetsFindByAssetLocation(String.valueOf(assetLocation));

            return ResponseEntity.ok(assetLocationData);
        } catch (IllegalArgumentException e) {
            // If the location name doesn't match, respond with a 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid location name: " + assetLocationStr);
        } catch (Exception e) {
            // Log the error and return a 500 Internal Server Error if something else goes wrong
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}
