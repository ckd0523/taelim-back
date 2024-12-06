package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.AssetClassificationAmountDto;
import com.codehows.taelim.dto.ByDepartmentAmountDto;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final CommonAssetRepository commonAssetRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final AssetSurveyHistoryRepository assetSurveyHistoryRepository;
    private final CommonAssetRepositoryCustomImpl commonAssetRepositoryCustom;

    //소유 자산 총액 가져오기
    public Long getTotalOwnedPurchaseCost() {
        Long totalOwnedPurchaseCost = commonAssetRepository.findTotalOwnedPurchaseCost();

        return totalOwnedPurchaseCost;
    }

    //    국책 + 기타 자산 총액 가져오기
    public Long getTotalLeasedPurchaseCost() {
        Long totalLeasedPurchaseCost = commonAssetRepository.findTotalLeasedPurchaseCost();

        return totalLeasedPurchaseCost;
    }

    //유지보수 중인 레코드 개수 가져오기
    public Long getRepairingAmount() {
        Long repairingAmount = repairHistoryRepository.findRepairingAmount();

        return repairingAmount;
    }

    //자산 조사 중인 레코드 가져오기
    public Long getSurveyAmount() {
        Long surveyAmount = assetSurveyHistoryRepository.findSurveyAmount();

        return surveyAmount;
    }

    //총 자산 개수 가져오기
    public Long getTotalAssetAmount() {
        Long totalAssetAmount = commonAssetRepository.findTotalAssetAmount();

        return totalAssetAmount;
    }

    //자산별 자산개수
    public Map<AssetClassification, Long> getAssetClassificationAmount() {
        return commonAssetRepositoryCustom.assetsClassificationCounts();
    }

    //부서별 자산개수
    public Map<Department, Long> getByDepartmentAmount() {
        return commonAssetRepositoryCustom.departmentLongMap();
    }

    //부서별 중 자산별 개수
    public Map<Department, Map<AssetClassification , Long>> getDepartmentAssetClassificationAmount() {
        return commonAssetRepositoryCustom.getDepartmentAssetClassificationAmount();
    }

//    //가동별 자산 개수
//    public Map<OperationStatus, Long> getOperationAmount() {
//        Map<OperationStatus, Long> operationStatusAmount = new EnumMap<>(OperationStatus.class);
//
//        for(OperationStatus operationStatus : OperationStatus.values()) {
//            Long count = commonAssetRepository.countByOperationStatus(operationStatus);
//            operationStatusAmount.put(operationStatus, count);
//        }
//
//        return operationStatusAmount;
//    }

    //가동별 자산 개수
    public Map<OperationStatus, Long> getOperationStatusAmount() {
        return commonAssetRepositoryCustom.findOprtationStatusCounts();
    }
    //소유별 자산 개수
    public Map<Ownership, Long> getOwnershipAmount() {


        return commonAssetRepositoryCustom.findOwnershipCounts();
    }

    //총액추이
    public Map<Integer, Long> getPurchaseCost(int year) {

        return commonAssetRepositoryCustom.findAssetPurchaseSum(year);
    }

    //등급별 자산 개수
    public Map<String, Long> getAssetGrades() {

        return commonAssetRepositoryCustom.getAssetGrades();


    }

    //폐기가 다가오는 자산의 개수
    public Map<AssetClassification, Long> getAssetNearEndOfLifeCount() {
        return commonAssetRepositoryCustom.findAssetsNearEndOfLife();
    }

    //위치별 자산 개수
// Service call to fetch asset count by location
    public Map<AssetClassification, Long> getAssetsFindByAssetLocation(String assetLocation) {
        // Ensure assetLocation is valid before calling the method
        try {
            AssetLocation location = AssetLocation.valueOf(assetLocation);
            return commonAssetRepositoryCustom.findAssetsByAssetLocation(location);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AssetLocation: " + assetLocation);
        }
    }



}
