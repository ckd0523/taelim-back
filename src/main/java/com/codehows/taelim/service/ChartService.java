package com.codehows.taelim.service;

import com.codehows.taelim.dto.AssetTotalAmountDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.AssetSurveyHistoryRepository;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.CommonAssetRepositoryCustom;
import com.codehows.taelim.repository.RepairHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final CommonAssetRepository commonAssetRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final AssetSurveyHistoryRepository assetSurveyHistoryRepository;

    //소유 자산 총액 가져오기
    public Long getTotalOwnedPurchaseCost() {
        Long totalOwnedPurchaseCost = commonAssetRepository.findTotalOwnedPurchaseCost();

        return totalOwnedPurchaseCost;
    }

    //    //임대 자산 총액 가져오기
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
}
