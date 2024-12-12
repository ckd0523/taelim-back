package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.AmountSet;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.service.*;
//import com.codehows.taelim.service.QRService;
import com.codehows.taelim.service.QRService;
import com.codehows.taelim.service.RegisterService;
import com.codehows.taelim.service.UpdateService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
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

    private final QRService qrCodeService;
    private final UpdateService updateService;
    private final RegisterService registerService;
    private final AssetFinalService assetFinalService;
    private final CommonAssetRepository commonAssetRepository;
    private final DemandService demandService;
    private final EmailServcie emailServcie;
    private final AmountSetService amountSetService;

    //QR 생성하는곳
    @PostMapping("/generateQRCode")
    public ResponseEntity<String> generateQRCode(@RequestBody List<Long> assetNo) {
        for(Long id : assetNo) {
            qrCodeService.PrintAssetLabel(id);
        }
        return ResponseEntity.ok("");
    }


//    //QR 조회
//    @GetMapping("/{assetCode}")
//    public ResponseEntity<String> getQRCode(@PathVariable("assetCode") String assetCode) {
//        assetService.
//    }

    private final AssetService assetService;

    @PostMapping("/dispose/{assetCode}")
    public ResponseEntity<CommonAsset> disposeAsset (@PathVariable("assetCode") String assetCode){
        assetService.DisposeApprove(assetCode);
        return ResponseEntity.ok().build();
    }

    // 자산조회 : 휴지통 - 자산 폐기 동작 (자산 하나 폐기) - ADMIN 권한임 (권)
    @PostMapping("/disposeAsset/{assetCode}")
    public ResponseEntity<AssetUpdateResponse> disposeitem (
            @PathVariable("assetCode") String assetCode, @RequestBody AssetUpdateDto assetUpdateDto) {
        registerService.DisposeAsset(assetCode, assetUpdateDto);
        return ResponseEntity.ok().build();
    }

    // 자산조회 : 휴지통 - 자산 폐기 요청 동작 (자산 하나 폐기) - AssetManager 자산담당자가 폐기 요청 (권)
    // Expand 폴더 - AssetPageExpand.jsx 자산 조회 에서 자산폐기요청 동작
    @PostMapping("/disposeDemand/{assetCode}")
    public ResponseEntity<CommonAsset> disposedemand1 (
            @PathVariable("assetCode") String assetCode, @RequestBody AssetUpdateDto assetUpdateDto ) throws MessagingException {
        registerService.DisposeDemand(assetCode, assetUpdateDto);
        //이메일 보낼 예정
        emailServcie.sendEmail("자산 폐기 요청", "자산 폐기 요청이 있습니다. 확인해주세요." );
        return ResponseEntity.ok().build();
    }

    //상세조회 (공통 및 서브 칼럼) QR조회 화면
    @GetMapping("/asset1/{assetCode}")
    public Map<String, Object> getAssetDetail2 (@PathVariable("assetCode") String assetCode){
         return assetService.getAssetDetail2(assetCode);

    }

    // 자산 이력 - 폐기이력 테이블 - List 불러오기 (권)
    @GetMapping("/deleteHistory")
    public List<DeleteHistoryDto> getDeleteHistory () {
        return assetFinalService.getDeleteHistory();
    }

    // 자산 이력 - 수정이력 테이블 - List 불러오기 (권)
    @GetMapping("/updateHistory")
    public List<UpdateHistoryDto> getUpDateHistory() {
        return assetFinalService.getUpDateHistory();
    }

    // 수정 이력 - 테이블 선택 시 - 모달창에 관한 수정 전, 수정 후 (approve) 내용 가져오기 (권)
    @GetMapping("/list/{assetNo}")
    public ResponseEntity<List<AssetDto>> getAssetList(@PathVariable Long assetNo) {
        System.out.println("Requested assetNo: " + assetNo); // 로그 추가
        List<AssetDto> assets = assetFinalService.getLatestAndPreviousAssets(assetNo);
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
                    System.out.println("디버그 : " + assetDto.getAssetCode() +"디버그 2 : "+ assetDto.getAssetNo());
                    demandService.beforeDemand(assetDto.getAssetCode(), newAssetNo);

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
            //이메일 보낼 예정
            emailServcie.sendEmail("일괄 수정 요청", "일괄 수정요청이 있습니다. 확인해주세요." );
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

                demandService.beforeDemand(assetDto.getAssetCode(), newAssetNo);
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
            //이메일 보낼 예정
            emailServcie.sendEmail("일괄 폐기 요청", "일괄 폐기 요청이 있습니다. 확인해주세요." );
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

    // 자산 수정 시, 수정 안에 들어있는 파일 업로드 동작 (권)
    @PostMapping("/{assetCode}/files")
    public ResponseEntity<String> updateAssetFiles(
            @PathVariable String assetCode,
            @RequestParam("files") List<MultipartFile> newFiles,
            @RequestParam("fileType") List<FileType> fileTypes) {

        // 파일 수와 fileType 수가 일치하는지 확인
        if (newFiles.size() != fileTypes.size()) {
            return ResponseEntity.badRequest().body("파일 수와 파일 타입 수가 일치해야 합니다.");
        }

        try {
            // 파일 업데이트 서비스 호출
            registerService.updateAssetFiles(assetCode, newFiles, fileTypes);
            return ResponseEntity.ok("파일이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            // 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // 자산조회 - 분류별, 검색, 페이지네이션 처리 - 상세내용들도 다 들고있음(권)
    @GetMapping("/getAssetSearch")
    public ResponseEntity<PaginatedResponse<AssetDto>> searchAssets(
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) AssetLocation assetLocationEnum,  // Enum 추가
            @RequestParam(required = false) String assetUser,
            @RequestParam(required = false) Department departmentEnum,  // Enum 추가
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) AssetClassification assetClassification,
            @RequestParam(required = false) String valueStandardNo,  // 새로 추가된 필드
            @RequestParam(required = false) Ownership ownership,
            @RequestParam(required = false) OperationStatus operationStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        AmountSetDto amountSetDto = null;

        // valueStandardNo 값 처리
        if (valueStandardNo != null) {
            AmountSet amountSet = amountSetService.getAmountSetByNo(1L);  // valueStandardNo는 항상 1로 처리

            if (amountSet != null) {
                // 이미 생성된 amountSetDto가 없으면 새로 생성
                if (amountSetDto == null) {
                    amountSetDto = new AmountSetDto();
                }

                if ("high".equals(valueStandardNo)) {
                    amountSetDto.setHigh_value_standard(amountSet.getHighValueStandard());
                } else if ("low".equals(valueStandardNo)) {
                    amountSetDto.setLow_value_standard(amountSet.getLowValueStandard());
                } else if ("medium".equals(valueStandardNo)) {
                    amountSetDto.setHigh_value_standard(amountSet.getHighValueStandard());
                    amountSetDto.setLow_value_standard(amountSet.getLowValueStandard());
                    System.out.println("Medium Standard Values: Low = " + amountSetDto.getLow_value_standard() + ", High = " + amountSetDto.getHigh_value_standard());
                }
            }
        }
        // 검색 결과를 가져옵니다.
        PaginatedResponse<AssetDto> response = assetFinalService.getAssetSearch(
                assetName,assetLocationEnum,
                assetUser, departmentEnum,
                startDate, endDate, assetClassification,amountSetDto, ownership, operationStatus, page, size
        );

        return ResponseEntity.ok(response);
    }

    // 엑셀 출력 전 - List 생기는지 확인(권)
    @GetMapping("/assets/excel")
    public ResponseEntity<List<CommonAsset>> getAssetsByClassification(
            @RequestParam(required = false)  AssetClassification assetClassification) {

        try {
            List<CommonAsset> assets = assetFinalService.listAssetByExcel(assetClassification);
            return ResponseEntity.ok(assets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 요청 처리
        }
    }

    // 엑셀 출력 (권)
    @GetMapping("/assets/export")
    public ResponseEntity<byte[]> exportAssetsToExcel(
            @RequestParam(required = false) String assetClassification) {
        try {
            byte[] excelFile = assetFinalService.exportAssetsToExcel(assetClassification);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=assets.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelFile);
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

