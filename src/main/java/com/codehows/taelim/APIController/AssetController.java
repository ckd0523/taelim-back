package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.dto.AssetUpdateDto;
import com.codehows.taelim.dto.AssetUpdateResponse;
import com.codehows.taelim.dto.ExcelDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import com.codehows.taelim.service.AssetService;
import com.codehows.taelim.service.FileService;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/asset")
public class AssetController {

    private final RegisterService registerService;
    private final FileService fileService;

    // assetCheck 폴더 - AssetRegister.jsx 등록 api
    @PostMapping("/register")
    public ResponseEntity<Long> registerAsset(@RequestBody AssetDto assetDto) {

        System.out.println(assetDto.getAssetClassification()+"여기");
        Long assetNo = registerService.assetRegister(assetDto);

        return new ResponseEntity<>(assetNo, HttpStatus.OK);

    }

    // Expand 폴더 - RowDetails.jsx 자산 조회  - 자산 1개 수정 동작 - ADMIN권한 (권)
    @PostMapping("/update/{assetCode}")
    public ResponseEntity<String> updateAsset(
            @PathVariable String assetCode,
            @RequestBody AssetUpdateDto assetDto) {

        try {
            // 서비스 호출
            AssetUpdateResponse response = registerService.updateAssetCode(assetCode, assetDto);

            // 자산 번호가 null이면 UNCONFIRMED 상태이므로 경고 메시지 반환
            if (response.getAssetNo() == null) {
                return ResponseEntity.ok(response.getMessage());  // 메시지 2개 보냄
            }

            // 정상적으로 자산 번호가 있으면 자산 수정 성공 메시지 반환
            return ResponseEntity.ok(response.getMessage());
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    // Expand 폴더 - RowDetails.jsx 자산 조회  - 자산 1개 수정 요청 동작 - AssetManager권한 (권)
    @PostMapping("/updateDemand/{assetCode}")
    public ResponseEntity<String> updateDemand(
            @PathVariable String assetCode,
            @RequestBody AssetUpdateDto assetDto) {

        try {
            // 서비스 호출
            AssetUpdateResponse response = registerService.updatedemandAssetCode(assetCode, assetDto);

            // 자산 번호가 null이면 UNCONFIRMED 상태이므로 경고 메시지 반환
            if (response.getAssetNo() == null) {
                return ResponseEntity.ok(response.getMessage());  // 메시지 2개 보냄
            }
            //이메일 보낼 예정
            //emailServcie.sendEmail("", "" );
            // 정상적으로 자산 번호가 있으면 자산 수정 성공 메시지 반환
            return ResponseEntity.ok(response.getMessage());
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("assetNo") Long assetNo, @RequestParam("fileType") String fileType) {

        CommonAsset asset = registerService.findById(assetNo).orElseThrow(() -> new RuntimeException("자산을 찾을 수 없습니다."));

        FileType type;
        try{
            type = FileType.valueOf(fileType);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("잘못된 파일 유형입니다", HttpStatus.BAD_REQUEST);
        }
        System.out.println("Found asset: " + asset);
        File savedFile = fileService.upload(file, asset, type);
        if (savedFile != null) {
            return new ResponseEntity<>("파일이 성공적으로 업로드되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("파일 업로드에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // ExcelAssetRegister 폴더 - ExcelRegister.jsx 엑셀 등록 api
    @PostMapping("/excelRegister")
    public ResponseEntity<?> uploadExcelData(@RequestBody List<AssetDto> excelDtos) {
        try{
            System.out.println("Received excel : "+ excelDtos);

//            return new ResponseEntity<>( , HttpStatus.OK);
        }catch (Exception e) {
//            return new ResponseEntity<>("엑셀 데이터 업로드 실패 " , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        registerService.excelRegisterAll(excelDtos);

        return new ResponseEntity<>("엑셀데이터 정상적으로 업로드 " ,HttpStatus.OK);
    }
}
