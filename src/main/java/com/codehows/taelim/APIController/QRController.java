package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.AssetService;
import com.codehows.taelim.service.QRService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@RestController
public class QRController {

    private final QRService qrCodeService;

    //QR 생성하는곳
    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String assetCode) {
        try {
            // 자산 코드에 기반하여 QR 코드를 생성합니다.
            String url = "http://133.186.153.78:8080/asset/" + assetCode;
            // String code = assetCode; // QR 코드에 포함할 텍스트를 설정합니다.
            byte[] qrCode = qrCodeService.generateQRCode(url, 200, 200); // QR 코드 생성

            // 응답 헤더를 설정합니다. 콘텐츠 타입을 PNG 이미지로 설정합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            // QR 코드 바이트 배열을 응답 본문으로 설정하여 반환합니다.
            return ResponseEntity.ok().headers(headers).body(qrCode);
        } catch (WriterException | IOException e) {
            // QR 코드 생성 중 오류가 발생한 경우, 500 Internal Server Error 응답을 반환합니다.
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/generateQRCodeBatch")
    public ResponseEntity<Map<String, String>> generateQRCodeBatch(@RequestBody List<String> assetCodes) {
        try {
            Map<String, String> qrCodeMap = new HashMap<>();

            for (String assetCode : assetCodes) {
                String url = "http://133.186.153.78:8080/asset/" + assetCode;
                byte[] qrCode = qrCodeService.generateQRCode(url, 200, 200);
                String base64Image = Base64.getEncoder().encodeToString(qrCode);
                qrCodeMap.put(assetCode, "data:image/png;base64," + base64Image);
            }

            return ResponseEntity.ok(qrCodeMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/generateQRCodePDF")
    public ResponseEntity<byte[]> generateQRCodePDF(@RequestBody List<String> assetCodes) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            int yPosition = 700;

            for (String assetCode : assetCodes) {
                String url = "http://133.186.153.78:8080/asset/" + assetCode;
                byte[] qrCode = qrCodeService.generateQRCode(url, 200, 200);

                // 바이트 배열을 BufferedImage로 변환
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qrCode));

                // BufferedImage를 PDFBox의 PDImageXObject로 변환
                PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
                contentStream.drawImage(pdImage, 100, yPosition, 200, 200);

                yPosition -= 220; // 다음 QR 코드의 Y 위치를 조정합니다.
            }

            contentStream.close();
            document.save(byteArrayOutputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename("qrcodes.pdf").build());

            return ResponseEntity.ok().headers(headers).body(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


//    //QR 조회
//    @GetMapping("/{assetCode}")
//    public ResponseEntity<String> getQRCode(@PathVariable("assetCode") String assetCode) {
//
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
        public ResponseEntity<CommonAsset> disposeitem (@PathVariable("assetCode") String
        assetCode, @RequestBody AssetDisposeDto assetDisposeDto){
            assetService.DisposeAsset(assetCode, assetDisposeDto);
            return ResponseEntity.ok().build();
        }

        // 자산담당자가 폐기 요청
        @PostMapping("/disposeDemand/{assetCode}")
        public ResponseEntity<CommonAsset> disposedemand (@PathVariable("assetCode") String
        assetCode, @RequestBody AssetDisposeDto assetDisposeDto){
            assetService.DisposeDemand(assetCode, assetDisposeDto);
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
        @GetMapping("/list/{assetCode}")
        public ResponseEntity<List<AssetDto>> getAssetList(@PathVariable String assetCode) {
            List<AssetDto> assets = assetService.getLatestAndPreviousAssets(assetCode);
            return ResponseEntity.ok(assets);
        }

    }

