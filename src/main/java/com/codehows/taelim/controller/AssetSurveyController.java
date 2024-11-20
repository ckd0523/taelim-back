package com.codehows.taelim.controller;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.security.PasswordHasher;
import com.codehows.taelim.security.PasswordHasher2;
import com.codehows.taelim.service.AssetSurveyService;
import com.codehows.taelim.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AssetSurveyController {

    private final AssetSurveyService assetSurveyService;
    private final PasswordHasher passwordHasher;
    private final PasswordHasher2 passwordHasher2;
    private final UserService userService;

    //자산 조사 이력 보여주기
    @GetMapping("/assetSurveyHistory")
    public List<AssetSurveyHistoryDto> getAssetSurveyHistory() {
//        SecureRandom rng = new SecureRandom();
//        byte[] hash = null;
//        try {
//            hash = passwordHasher2.hashPasswordV3("Programming is fun!", rng, "PBKDF2WithHmacSHA256", 10000, 16, 32);
//
//            // 예시 해시된 비밀번호
//            byte[] hashedPassword = hash;
//            // 출력 테스트
//            System.out.println(Base64.getEncoder().encodeToString(hash));
//
//            // 입력된 패스워드
//            String password = "Programming is fun!";
//            int[] iterCount = new int[1];  // 반복 횟수를 저장할 배열
//            String prf = "PBKDF2WithHmacSHA256"; // 사용된 PRF
//
//            boolean isValid = passwordHasher2.verifyHashedPasswordV3(hashedPassword, password, iterCount, prf);
//            System.out.println("패스워드 유효성 검사: " + isValid);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        return assetSurveyService.getAssetSurveyHistory();
    }

    //자산 조사 등록
    @PostMapping("/register")
    public ResponseEntity<Void> createAssetSurvey(@RequestBody AssetSurveyHistoryRegisterDto assetSurveyHistory) {
        System.out.println("프론트에서 넘어온 위치 : " + assetSurveyHistory.getLocation());
        System.out.println("프론트에서 온 자산 조사자 : "+assetSurveyHistory.getUserId());
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
    public ResponseEntity<Void> deleteAssetSurvey(@RequestBody DeleteRequestDto deleteRequestDto) {
        //프론트에서 넘어오는 List의 값이 integer 형식
        //System.out.println(deleteRequest.getAssetSurveyNo());
        //integer를 long으로 변환하기 위해 새 객체 생성하여 밑에서 변환
        List<Long> assetSurveyNos = new ArrayList<>();

        for (Integer integer : deleteRequestDto.getAssetSurveyNo()) {
            assetSurveyNos.add(integer.longValue()); // 각 Integer를 Long으로 변환 후 추가
        }
        //System.out.println("자산 조사 번호 잘 넘어왔나 : " + assetSurveyNos);

        //System.out.println(assetSurveyNos.get(0).getClass().getTypeName());
        //변환된 List<Long>을 매개변수로 넘겨줌
        boolean result = assetSurveyService.deleteAssetSurveyHistory(assetSurveyNos);
        //System.out.println(result);

        if(result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //자산 조사 상세 이력
    @GetMapping("/assetSurveyDetail/{assetSurveyNo}")
    public List<AssetSurveyDetailDto> getAssetSurveyDetail(@PathVariable("assetSurveyNo") Integer assetSurveyNo) {
        //System.out.println("여기라고!!!");
        //System.out.println("얘 아무것도 없어? : " + assetSurveyService.getAssetSurveyDetail((long)assetSurveyNo));
        List<AssetSurveyDetailDto> a = assetSurveyService.getAssetSurveyDetail((long)assetSurveyNo);
        for(AssetSurveyDetailDto dto : a) {
            //System.out.println("여기입니다" + dto.getAssetName());
            UserDto owner = userService.getUserById(dto.getAssetOwner());
            UserDto securityManager = userService.getUserById(dto.getAssetSecurityManager());

            // 자산 소유자 이름 설정
            dto.setAssetOwner(owner != null && !Objects.equals(owner.getFullname(), "")
                    ? owner.getFullname()
                    : "Unknown User");

            // 자산 보안 관리자 이름 설정
            dto.setAssetSecurityManager(securityManager != null && !Objects.equals(securityManager.getFullname(), "")
                    ? securityManager.getFullname()
                    : "Unknown User");
        }
        System.out.println(a);
        return a;
    }

    @GetMapping("/checkLocation/{selectedLocation}")
    //ResponseEntity에 ?는 아무 타입이나 가능하다는 의미
    public ResponseEntity<?> checkLocation(@PathVariable("selectedLocation") String selectedLocation) {
        //System.out.println("프론트에서 넘어온 위치 : " + selectedLocation);
        //String을 Enum으로 변환
        AssetLocation location = AssetLocation.valueOf(selectedLocation);
        boolean exists = assetSurveyService.checkLocation(location);
        //System.out.println("여기에서 문제인가?");

        if (exists) {
            // 이미 자산 조사가 있을 경우 HTTP 409(Conflict) 상태 코드와 함께 응답
            //conflict는 클라이언트가 요청한 작업이 서버의 현재 리소스 상태와 일치하지 않거나 충돌이 발생할 경우 사용
            return ResponseEntity.status(HttpStatus.CONFLICT).body("현재 위치에 대한 자산조사가 이미 있습니다.");
        } else {
            // 회차 정보를 가져오는 로직
            Long nextRound = assetSurveyService.getNextRoundForLocation(location);
            //Long nextRound = 3L;
            // 회차 정보를 포함한 응답 (JSON 형태로 클라이언트에 전송)
            return ResponseEntity.ok(nextRound);
        }
    }

    //자산 조사 완료
    @PutMapping("/completeSurvey/{assetSurveyNo}")
    //프론트에서 넘어오는 값이 int라서 Long으로 못받는 줄 알았는데 자동 변환 해주네;;
    public ResponseEntity<Void> completeSurvey(@PathVariable("assetSurveyNo") Long assetSurveyNo) {
        //System.out.println("자산 조사 번호 잘 오나 : " + assetSurveyNo);
        boolean confirm = assetSurveyService.completeSurvey(assetSurveyNo);
        if(confirm) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //자산 조사 수정(정위치 유무, 상태 변경 시 업데이트)
    @PutMapping("/updateAssetSurveyDetail")
    public ResponseEntity<Void> updateAssetSurveyDetail(@RequestBody AssetSurveyUpdateDto updateDto) {
        //dto에 있는 lombok이 boolean은 get으로 안하고 is...으로 함 레전드 처음 알았네
        //System.out.println("자산 조사 상세 타입 : " + updateDto.isRequestType());
        //System.out.println("자산 조사 상세 값 : " + updateDto.isUpdateValue());
        //System.out.println("자산 조사 상세 번호 잘 오나 : " + updateDto.getInfoNo());
        //System.out.println("자산 조사 상세 내용 : " + updateDto.getContent());

        boolean confirm = assetSurveyService.updateAssetSurveyDetail(updateDto);
        if(confirm) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //자산 조사 수정2(내용 변경 시 업데이트)
    @PutMapping("/updateAssetSurveyDetail2")
    public ResponseEntity<Void> updateAssetSurveyDetail2(@RequestBody AssetSurveyUpdateDto updateDto) {
        //dto에 있는 lombok이 boolean은 get으로 안하고 is...으로 함 레전드 처음 알았네
        //System.out.println("자산 조사 상세 타입 : " + updateDto.isRequestType());
        //System.out.println("자산 조사 상세 값 : " + updateDto.isUpdateValue());
        //System.out.println("자산 조사 상세 번호 잘 오나 : " + updateDto.getInfoNo());
        //System.out.println("자산 조사 상세 내용 : " + updateDto.getContent());

        boolean confirm = assetSurveyService.updateAssetSurveyDetail(updateDto);
        if(confirm) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
