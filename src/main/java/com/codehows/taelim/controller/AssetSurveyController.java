package com.codehows.taelim.controller;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.dto.AssetSurveyHistoryRegisterDto;
import com.codehows.taelim.dto.DeleteRequest;
import com.codehows.taelim.entity.AssetSurveyDetail;
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

    //자산 조사 이력 보여주기
    @GetMapping("/assetSurveyHistory")
    public List<AssetSurveyHistoryDto> getAssetSurveyHistory() {
        return assetSurveyService.getAssetSurveyHistory();
    }

    //자산 조사 등록
    @PostMapping("/register")
    public ResponseEntity<Void> createAssetSurvey(@RequestBody AssetSurveyHistoryRegisterDto assetSurveyHistory) {
        //postService.createPost(post);
        //assetSurveyService.assetSurveyRegister(AssetLocation.MAIN_1F, 1L, "user10@example.com");
        Boolean result = assetSurveyService.assetSurveyRegister(assetSurveyHistory);
        System.out.println(result);
        if(result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //삭제하는거라 deleteMapping으로 했드만
    //계속 RequestBody Missing이라고 떠서 Post로 바꾸니까 됨
    //아 알았따 deleteMapping은 @RequestBody를 못받음
    @PostMapping("/deleteAssetSurvey")
    public ResponseEntity<Void> deleteAssetSurvey(@RequestBody DeleteRequest deleteRequest) {
        //프론트에서 넘어오는 List의 값이 integer 형식
        System.out.println(deleteRequest.getAssetSurveyNo());
        //integer를 long으로 변환하기 위해 새 객체 생성하여 밑에서 변환
        List<Long> assetSurveyNos = new ArrayList<>();

        for (Integer integer : deleteRequest.getAssetSurveyNo()) {
            assetSurveyNos.add(integer.longValue()); // 각 Integer를 Long으로 변환 후 추가
        }

        //System.out.println(assetSurveyNos.get(0).getClass().getTypeName());
        //변환된 List<Long>을 매개변수로 넘겨줌
        String result = assetSurveyService.deleteAssetSurveyHistory(assetSurveyNos);
        System.out.println(result);

        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환
    }

    @GetMapping("/assetSurveyDetail")
    public List<AssetSurveyDetail> getAssetSurveyDetail(@RequestParam Integer assetSurveyNo) {
        return assetSurveyService.getAssetSurveyDetail((long)assetSurveyNo);
    }
}