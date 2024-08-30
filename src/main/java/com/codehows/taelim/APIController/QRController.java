package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.AssetService;
import com.codehows.taelim.service.QRService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


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
            String url = "http://localhost:8080/asset/" + assetCode;
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
                String url = "http://localhost:8080/asset/" + assetCode;
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


//    @PostMapping("/generateQRCodePDF")
//    public ResponseEntity<byte[]> generateQRCodePDF(@RequestBody List<String> assetCodes) {
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//             PDDocument document = new PDDocument()) {
//
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//            for (String assetCode : assetCodes) {
//                String url = "http://localhost:8080/asset/" + assetCode;
//                byte[] qrCode = qrCodeService.generateQRCode(url, 200, 200);
//
//                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, qrCode, assetCode);
//                contentStream.drawImage(pdImage, 100, 700 - (assetCodes.indexOf(assetCode) * 220), 200, 200);
//            }
//            contentStream.close();
//            document.save(byteArrayOutputStream);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDisposition(ContentDisposition.inline().filename("qrcodes.pdf"));
//
//            return ResponseEntity.ok().headers(headers).body(byteArrayOutputStream.toByteArray());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }



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
    public Optional<CommonAsset> getCommonAsset(@PathVariable("assetCode") String assetCode) {
        return assetService.getCommonAsset(assetCode);
    }

    //상세조회 (공통 및 서브 칼럼)
    @GetMapping("/asset/{assetCode}")
    public Map<String, Object> getAssetDetail(@PathVariable("assetCode") String assetCode) {
        return assetService.getAssetDetail(assetCode);
    }

    @PostMapping("/dispose/{assetCode}")
    public ResponseEntity<CommonAsset> disposeAsset(@PathVariable("assetCode") String assetCode) {
        assetService.DisposeApprove(assetCode);
        return ResponseEntity.ok().build();
    }

}
