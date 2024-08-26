package com.codehows.taelim.controller;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.dto.AssetSurveyHistoryRegisterDto;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.service.AssetSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> createPost(@RequestBody AssetSurveyHistoryRegisterDto assetSurveyHistory) {
        //postService.createPost(post);
        //assetSurveyService.assetSurveyRegister(AssetLocation.MAIN_1F, 1L, "user10@example.com");
        String result = assetSurveyService.assetSurveyRegister(assetSurveyHistory.getLocation(), assetSurveyHistory.getRound(), assetSurveyHistory.getEmail());
        System.out.println(result);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환
    }
}
