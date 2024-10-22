package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.service.AssetFinalService;
import com.codehows.taelim.service.AssetService;
//import com.codehows.taelim.service.QRService;
import com.codehows.taelim.service.RegisterService;
import com.codehows.taelim.service.UpdateService;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@RestController
public class QRController {

//    private final QRService qrCodeService;
    private final UpdateService updateService;
    private final RegisterService registerService;
    private final AssetFinalService assetFinalService;
    private final CommonAssetRepository commonAssetRepository;

    //QR 생성하는곳
//    @PostMapping("/generateQRCode")
//    public ResponseEntity<String> generateQRCode(@RequestBody List<Long> assetNo) {
//        for(Long id : assetNo) {
//            qrCodeService.PrintAssetLabel(id);
//        }
//        return ResponseEntity.ok("");
//    }


//    //QR 조회
//    @GetMapping("/{assetCode}")
//    public ResponseEntity<String> getQRCode(@PathVariable("assetCode") String assetCode) {
//        assetService.
//    }

    private final AssetService assetService;
    
    //목록 조회
    @GetMapping("/assets/approved-not-disposed")
    public List<AssetDto> getApprovedAndNotDisposedAssets() {
        return assetService.getApprovedAndNotDisposedAssets();
    }

    //상세조회 (공통 칼럼)
    @GetMapping("/assets/{assetCode}")
    public CommonAssetDto getCommonAsset(@PathVariable("assetCode") String assetCode) {
        CommonAsset commonAsset = assetService.getCommonAsset(assetCode).orElse(null);
        CommonAssetDto commonAssetDto = new CommonAssetDto();
        commonAssetDto.setAssetCode(assetCode);
        commonAssetDto.setAssetName(commonAsset.getAssetName());
        commonAssetDto.setAssetBasis(commonAsset.getAssetBasis());
        commonAssetDto.setAssetClassification(commonAsset.getAssetClassification());
        commonAssetDto.setAssetNo(commonAsset.getAssetNo());
        return commonAssetDto;
    }


//    //상세조회 (공통 및 서브 칼럼)
//    @GetMapping("/asset/{assetCode}")
//    public Map<String, Object> getAssetDetail(@PathVariable("assetCode") String assetCode) {
//        return assetService.getAssetDetail(assetCode);
//    }

    //상세조회 (공통 및 서브 칼럼)
    @GetMapping("/asset/{assetCode}")
    public AssetDto getAssetDetail(@PathVariable("assetCode") String assetCode) {
        return assetService.getAssetDetail(assetCode);
    }

        //상세조회 (공통 및 서브 칼럼)
        //@GetMapping("/asset/{assetCode}")
        // public AssetDto getAssetDetail(@PathVariable("assetCode") String assetCode) {
        //    return assetService.getAssetDetail(assetCode);
        //}

        //상세조회 (공통 및 서브 칼럼)
        @GetMapping("/asset1/{assetCode}")
        public Map<String, Object> getAssetDetail2 (@PathVariable("assetCode") String assetCode){
            return assetService.getAssetDetail2(assetCode);

        }

    //상세조회 (공통 및 서브 칼럼)
    @GetMapping("/test/{assetNo}")
    public CommonAsset getAssetDetail3 (@PathVariable("assetNo") Long assetNo){
        CommonAssetDto commonAssetDto = CommonAssetDto.fromEntity(commonAssetRepository.findById(assetNo).orElseThrow());
        commonAssetDto.setAssetNo(null);
        CommonAsset commonAsset = commonAssetDto.toEntity(commonAssetDto);
        CommonAsset commonAsset1 = commonAssetRepository.save(commonAsset);
        return commonAsset1;

    }

//    //상세조회 (공통 및 서브 칼럼)
//    @GetMapping("/asset/{assetCode}")
//    public Map<String, Object> getAssetDetail2(@PathVariable("assetCode") String assetCode) {
//        return assetService.getAssetDetail2(assetCode);
//    }

        @PostMapping("/dispose/{assetCode}")
        public ResponseEntity<CommonAsset> disposeAsset (@PathVariable("assetCode") String assetCode){
            assetService.DisposeApprove(assetCode);
            return ResponseEntity.ok().build();
        }


        // 자산관리자가 폐기
        @PostMapping("/disposeAsset/{assetCode}")
        public ResponseEntity<AssetUpdateResponse> disposeitem (
                @PathVariable("assetCode") String assetCode,
                @RequestBody AssetDisposeDto assetDisposeDto) {
            try {
                AssetUpdateResponse response = assetService.DisposeAsset(assetCode, assetDisposeDto);
                // 성공적인 응답을 위해 200 OK 상태 반환
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                // 오류 발생 시 400 Bad Request 상태 반환
                return ResponseEntity.badRequest().body(new AssetUpdateResponse(e.getMessage(), null));
            }
        }

        // 자산담당자가 폐기 요청
        @PostMapping("/disposeDemand/{assetCode}")
        public ResponseEntity<CommonAsset> disposedemand1 (@PathVariable("assetCode") String
        assetCode, @RequestBody AssetDisposeDto assetDisposeDto ){
            registerService.DisposeDemand(assetCode, assetDisposeDto);
            return ResponseEntity.ok().build();
        }

        // 폐기이력 불러오기
        @GetMapping("/deleteHistory")
        public List<DeleteHistoryDto> getDeleteHistory () {
            return assetService.getDeleteHistory();
        }

        // 수정이력 불러오기
        @GetMapping("/updateHistory")
        public List<UpdateHistoryDto> getUpDateHistory() {
            return assetService.getUpDateHistory();
        }

        // 자산 상세화면 가져오기
        @GetMapping("/list/{assetNo}")
        public ResponseEntity<List<AssetDto>> getAssetList(@PathVariable Long assetNo) {
            System.out.println("Requested assetNo: " + assetNo); // 로그 추가
            List<AssetDto> assets = assetService.getLatestAndPreviousAssets(assetNo);
            return ResponseEntity.ok(assets);
        }

    // 자산 상세화면 가져오기(요청 버전)
    @GetMapping("/list1/{assetNo}")
    public ResponseEntity<List<AssetDto>> getAssetList1(@PathVariable Long assetNo) {
        System.out.println("Requested assetNo: " + assetNo); // 로그 추가
        List<AssetDto> assets = assetService.getLatestAndPreviousAssets1(assetNo);
        return ResponseEntity.ok(assets);
    }

    // 수정요청 상세 가져오기
    @GetMapping("/updateDetail/{assetNo}")
    public ResponseEntity<List<AssetDto>> getUpdateDetail(@PathVariable Long assetNo) {
        System.out.println("Requested assetNo: " + assetNo); // 로그 추가
        List<AssetDto> assets = assetService.getUpdateDetail(assetNo);
        return ResponseEntity.ok(assets);
    }

        @PostMapping("/allUpdate")
        public ResponseEntity<String> allUpdate(@RequestBody AllUpdateDto updateToSend){
            try {
                List<AssetUpdateDto> assetDtos = updateToSend.getAssetDtos();
//                for (AssetUpdateDto assetDto : assetDtos) {
//                    Approval approval = assetService.demandCheck(assetDto.getAssetCode());
//                    System.out.println("자산의 처리 상태 확인"+approval);
//                    if (approval == Approval.UNCONFIRMED){
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                                .body("자산 코드 " + assetDto.getAssetCode() + "의 상태가 작업 진행중입니다. 해당 작업은 모두 취소됩니다.");
//                    }
//                }
                Demand demand = registerService.UpdateDemand(updateToSend);
                // 수정 이력 저장
                for (AssetUpdateDto assetDto : assetDtos) {
                    updateToSend.setAssetDto(assetDto);
                    updateToSend.setAssetNo(assetDto.getAssetNo());
                    Long newAssetNo = registerService.allUpdate(updateToSend, demand);
                }

                return ResponseEntity.ok("자산 수정 등록완료");
            } catch (Exception e) {
                // 예외 메시지 로깅
                e.printStackTrace();
                // 클라이언트에게 오류 메시지 전송
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
            }
        }
    @PostMapping("/allUpdateDemand")
    public ResponseEntity<String> allUpdateDemand(@RequestBody AllUpdateDto updateToSend){
        try {
            List<AssetUpdateDto> assetDtos = updateToSend.getAssetDtos();
//            for (AssetUpdateDto assetDto : assetDtos) {
//                Approval approval = assetService.demandCheck(assetDto.getAssetCode());
//                if (approval == Approval.UNCONFIRMED){
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("자산 코드 " + assetDto.getAssetCode() + "의 상태가 작업 진행중입니다. 해당 작업은 모두 취소됩니다.");
//                }
//            }
            Demand demand = registerService.UpdateDemand(updateToSend);
            for (AssetUpdateDto assetDto : assetDtos) {
                updateToSend.setAssetDto(assetDto);
                updateToSend.setAssetNo(assetDto.getAssetNo());
                Long newAssetNo = registerService.allUpdateDemand1(updateToSend, demand);
            }
            return ResponseEntity.ok("자산 수정 등록완료");
        } catch (Exception e) {
            // 예외 메시지 로깅
            e.printStackTrace();
            // 클라이언트에게 오류 메시지 전송
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    @PostMapping("/allDelete")
    public ResponseEntity<String> allDelete(@RequestBody AllDeleteDto disposeToSend){
        try {

            List<AssetUpdateDto> assetDtos = disposeToSend.getAssetDtos();
//            for (AssetUpdateDto assetDto : assetDtos) {
//                Approval approval = assetService.demandCheck(assetDto.getAssetCode());
//                System.out.println("자산의 처리 상태 확인"+approval);
//                if (approval == Approval.UNCONFIRMED){
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("자산 코드 " + assetDto.getAssetCode() + "의 상태가 작업 진행중입니다. 해당 작업은 모두 취소됩니다.");
//                }
//            }
            Demand demand = registerService.DeleteDemand(disposeToSend);
            for (AssetUpdateDto assetDto : assetDtos) {
                disposeToSend.setAssetDto(assetDto);
                disposeToSend.setAssetNo(assetDto.getAssetNo());
                Long newAssetNo = registerService.allDelete(disposeToSend, demand);
            }
            return ResponseEntity.ok("자산 폐기 등록완료");
        } catch (Exception e) {
            // 예외 메시지 로깅
            e.printStackTrace();
            // 클라이언트에게 오류 메시지 전송
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    @PostMapping("/allDeleteDemand")
    public ResponseEntity<String> allDeleteDemand(@RequestBody AllDeleteDto disposeToSend){
        try {
            List<AssetUpdateDto> assetDtos = disposeToSend.getAssetDtos();
//            for (AssetUpdateDto assetDto : assetDtos) {
//                Approval approval = assetService.demandCheck(assetDto.getAssetCode());
//                System.out.println("자산의 처리 상태 확인"+approval);
//                if (approval == Approval.UNCONFIRMED){
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("자산 코드 " + assetDto.getAssetCode() + "의 상태가 작업 진행중입니다. 해당 작업은 모두 취소됩니다.");
//                }
//            }
            Demand demand = registerService.DeleteDemand(disposeToSend);
            for (AssetUpdateDto assetDto : assetDtos) {
                disposeToSend.setAssetDto(assetDto);
                disposeToSend.setAssetNo(assetDto.getAssetNo());
                Long newAssetNo = registerService.allDeleteDemamd(disposeToSend, demand);
            }
            return ResponseEntity.ok("자산 폐기 등록완료");
        } catch (Exception e) {
            // 예외 메시지 로깅
            e.printStackTrace();
            // 클라이언트에게 오류 메시지 전송
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }


        // 자산 조회 - 상세정보 화면 까지 다 가져오는 테스트
        @GetMapping("/assets/test")
        public List<AssetDto> test() {
            return assetService.getAssetDetail3();
        }


    // 파일 업데이트 API
    @PostMapping("/{assetCode}/files")
    public ResponseEntity<String> updateAssetFiles(
            @PathVariable String assetCode,
            @RequestParam("files") List<MultipartFile> newFiles,
            @RequestParam("fileType") FileType fileType) {

        try {
            // 파일 업데이트 서비스 호출
            registerService.updateAssetFiles(assetCode, newFiles, fileType);
            return ResponseEntity.ok("파일이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            // 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    //검색 api
    @GetMapping("/getAssetSearch")
    public ResponseEntity<PaginatedResponse<AssetDto>> searchAssets(
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) String assetLocationString,  // String으로 변경
            @RequestParam(required = false) AssetLocation assetLocationEnum,  // Enum 추가
            @RequestParam(required = false) String assetUser,
            @RequestParam(required = false) String departmentString,  // String으로 변경
            @RequestParam(required = false) Department departmentEnum,  // Enum 추가
            @RequestParam(required = false) LocalDate introducedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 검색 결과를 가져옵니다.
        PaginatedResponse<AssetDto> response = assetFinalService.getAssetSearch(
                assetName, assetLocationString, assetLocationEnum,
                assetUser, departmentString, departmentEnum,
                introducedDate, page, size
        );

        return ResponseEntity.ok(response);
    }
}

