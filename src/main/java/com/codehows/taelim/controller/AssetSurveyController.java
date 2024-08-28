package com.codehows.taelim.controller;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.dto.AssetSurveyHistoryRegisterDto;
import com.codehows.taelim.dto.DeleteRequest;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.service.AssetSurveyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssetSurveyController {

    private final AssetSurveyService assetSurveyService;

    @GetMapping("/assetSurveyHistory")
    public List<AssetSurveyHistoryDto> getAssetSurveyHistory() {
        return assetSurveyService.getAssetSurveyHistory();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createAssetSurvey(@RequestBody AssetSurveyHistoryRegisterDto assetSurveyHistory) {
        //postService.createPost(post);
        //assetSurveyService.assetSurveyRegister(AssetLocation.MAIN_1F, 1L, "user10@example.com");
        String result = assetSurveyService.assetSurveyRegister(assetSurveyHistory.getLocation(), assetSurveyHistory.getRound(), assetSurveyHistory.getEmail());
        System.out.println(result);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환
    }

    @PostMapping("/deleteAssetSurvey")
    public ResponseEntity<Void> deleteAssetSurvey(@RequestBody DeleteRequest deleteRequest) {
        System.out.println(deleteRequest.getAssetSurveyNo());
        List<Long> assetSurveyNos = new ArrayList<>();

        for (Integer integer : deleteRequest.getAssetSurveyNo()) {
            assetSurveyNos.add(integer.longValue()); // 각 Integer를 Long으로 변환 후 추가
        }

        System.out.println(assetSurveyNos.get(0).getClass().getTypeName());
        String result = assetSurveyService.deleteAssetSurveyHistory(assetSurveyNos);
        System.out.println(result);

        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환
    }
}
