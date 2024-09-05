package com.codehows.taelim.service;

import com.codehows.taelim.dto.AssetSurvey;
import com.codehows.taelim.entity.AssetSurveyDetail;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.AssetSurveyDetailRepository;
import com.codehows.taelim.repository.AssetSurveyHistoryRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetSurveyService {

    private final AssetSurveyDetailRepository assetSurveyDetailRepository;
    private final AssetSurveyHistoryRepository assetSurveyHistoryRepository;

    public List<AssetSurvey> getAssetSurveysByAssetNo(CommonAsset assetNo) {
        // AssetSurveyDetail 엔티티 리스트를 가져옴
        List<AssetSurveyDetail> assetSurveyDetails = assetSurveyDetailRepository.findByAssetNo(assetNo);

        // AssetSurveyHistory를 찾기 위해 모든 assetSurveyNo를 수집
        List<Long> assetSurveyNos = assetSurveyDetails.stream()
                .map(detail -> detail.getAssetSurveyNo().getAssetSurveyNo())
                .collect(Collectors.toList());

        // 수집한 assetSurveyNo로 AssetSurveyHistory 엔티티 리스트를 가져옴
        List<AssetSurveyHistory> assetSurveyHistories = assetSurveyHistoryRepository.findByAssetSurveyNoIn(assetSurveyNos);

        // DTO로 변환
        List<AssetSurvey> assetSurveys = new ArrayList<>();
        for (AssetSurveyDetail detail : assetSurveyDetails) {
            AssetSurveyHistory history = assetSurveyHistories.stream()
                    .filter(h -> h.getAssetSurveyNo().equals(detail.getAssetSurveyNo().getAssetSurveyNo()))
                    .findFirst()
                    .orElse(null);

            if (history != null) {
                AssetSurvey dto = new AssetSurvey();
                dto.setAssetSurveyNo(history.getAssetSurveyNo());
                dto.setAssetSurveyBy(history.getAssetSurveyBy().getEmail()); // Assuming `Member` has a `name` property
                dto.setRound(history.getRound());
                dto.setAssetSurveyLocation(history.getAssetSurveyLocation());
                dto.setAssetSurveyStartDate(history.getAssetSurveyStartDate());
                dto.setAssetSurveyEndDate(history.getAssetSurveyEndDate());
                dto.setSurveyStatus(history.getSurveyStatus());

                dto.setAssetSurveyDetailNo(detail.getAssetSurveyDetailNo());
                dto.setAssetStatus(detail.getAssetStatus());
                dto.setExactLocation(detail.getExactLocation());
                dto.setAssetSurveyContent(detail.getAssetSurveyContent());

                assetSurveys.add(dto);
            }
        }

        return assetSurveys;
    }
}
