package com.codehows.taelim.service;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.constant.OperationStatus;
import com.codehows.taelim.constant.Ownership;
import com.codehows.taelim.dto.AssetClassificationAmountDto;
import com.codehows.taelim.dto.AssetTotalAmountDto;
import com.codehows.taelim.dto.ByDepartmentAmountDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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

    //자산별 자산개수
    public AssetClassificationAmountDto getAssetClassificationAmount() {
        return
        AssetClassificationAmountDto.builder()
        .informationProtectionSystemAmount(commonAssetRepository.countByAssetClassification(AssetClassification.INFORMATION_PROTECTION_SYSTEM))
        .applicationProgramAmount(commonAssetRepository.countByAssetClassification(AssetClassification.APPLICATION_PROGRAM))
        .softwareAmount(commonAssetRepository.countByAssetClassification(AssetClassification.SOFTWARE))
        .electronicInformationAmount(commonAssetRepository.countByAssetClassification(AssetClassification.ELECTRONIC_INFORMATION))
        .documentAmount(commonAssetRepository.countByAssetClassification(AssetClassification.DOCUMENT))
        .patentsAndTrademarksAmount(commonAssetRepository.countByAssetClassification(AssetClassification.PATENTS_AND_TRADEMARKS))
        .itSystemEquipAmount(commonAssetRepository.countByAssetClassification(AssetClassification.ITSYSTEM_EQUIPMENT))
        .itNetworkEquipAmount(commonAssetRepository.countByAssetClassification(AssetClassification.ITNETWORK_EQUIPMENT))
        .terminalAmount(commonAssetRepository.countByAssetClassification(AssetClassification.TERMINAL))
        .furnitureAmount(commonAssetRepository.countByAssetClassification(AssetClassification.FURNITURE))
        .devicesAmount(commonAssetRepository.countByAssetClassification(AssetClassification.DEVICES))
        .carAmount(commonAssetRepository.countByAssetClassification(AssetClassification.CAR))
        .otherAssetsAmount(commonAssetRepository.countByAssetClassification(AssetClassification.OTHERASSETS))
                .build();
    }

    //부서별 자산개수
    public ByDepartmentAmountDto getByDepartmentAmount() {
        return ByDepartmentAmountDto .builder()
                .managementPlanningAmount(commonAssetRepository.countByDepartment(Department.MANAGEMENT_PLANNING_OFFICE))
                .managementAmount(commonAssetRepository.countByDepartment(Department.MANAGEMENT_TEAM))
                .salesAmount(commonAssetRepository.countByDepartment(Department.SALES_TEAM))
                .purchaseAmount(commonAssetRepository.countByDepartment(Department.PURCHASE_TEAM))
                .qualityAmount(commonAssetRepository.countByDepartment(Department.QUALITY_TEAM))
        .productionAmount(commonAssetRepository.countByDepartment(Department.PRODUCTION_TEAM))
                .technologyResearchAmount(commonAssetRepository.countByDepartment(Department.TECHNOLOGY_RESEARCH_TEAM))
                .build();
    }

    //부서별 중 자산별 개수
    public Map<Department, Map<AssetClassification , Long>> getDepartmentAssetClassificationAmount() {
        Map<Department, Map<AssetClassification, Long>> departmentAssetClassificationAmount = new EnumMap<>(Department.class);


        for (Department department : Department.values()) {
            Map <AssetClassification, Long> assetClassificationAmount = new EnumMap<>(AssetClassification.class);

            for (AssetClassification assetClassification : AssetClassification.values()) {
                Long count = commonAssetRepository.countByDepartmentAndAssetClassification(department, assetClassification);
                assetClassificationAmount.put(assetClassification, count);
            }
            departmentAssetClassificationAmount.put(department, assetClassificationAmount);

        }

        return departmentAssetClassificationAmount;
    }

    //가동별 자산 개수
    public Map<OperationStatus, Long> getOperationAmount() {
        Map<OperationStatus, Long> operationStatusAmount = new EnumMap<>(OperationStatus.class);

        for(OperationStatus operationStatus : OperationStatus.values()) {
            Long count = commonAssetRepository.countByOperationStatus(operationStatus);
            operationStatusAmount.put(operationStatus, count);
        }

        return operationStatusAmount;
    }

    //소유별 자산 개수
    public Map<Ownership, Long> getOwnershipAmount() {
        Map<Ownership, Long> ownershipAmount = new EnumMap<>(Ownership.class);

        for(Ownership ownership : Ownership.values()){
            Long count = commonAssetRepository.countByOwnership(ownership);
            ownershipAmount.put(ownership, count);
        }

        return ownershipAmount;
    }

    //총액추이
    public Map<Integer, Long> getPurchaseCost() {

        return commonAssetRepositoryCustom.findAssetPurchaseSum();
    }

    //등급별 자산 개수
    public Map<String, Long> getAssetGrades() {
//        List<CommonAsset> assets = commonAssetRepository.findAll();
//        Map<String, Long> gradeCount = new HashMap<>();
//
//        for (CommonAsset asset: assets) {
//            int totalCount = asset.getConfidentiality() + asset.getIntegrity() + asset.getAvailability();
//            String grade = "";
//
//            if(totalCount >= 7 && totalCount <=9) {
//                grade = "A";
//            }else if(totalCount >=5 && totalCount <=6) {
//                grade = "B";
//            }else if(totalCount >=3 && totalCount <=4) {
//                grade = "C";
//            }
//
//            gradeCount.put(grade, gradeCount.getOrDefault(grade, 0L) + 1);
//
//        }

        return commonAssetRepositoryCustom.getAssetGrades();


    }
    //폐기가 다가오는 자산의 개수
    public Map<AssetClassification, Long> getAssetNearEndOfLifeCount(LocalDate referenceDate) {
        return commonAssetRepositoryCustom.findAssetsNearEndOfLife(referenceDate);
    }





}
