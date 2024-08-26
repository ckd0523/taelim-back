package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.AssetService;
import com.codehows.taelim.service.QRService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class QRController {

    private final QRService qrCodeService;

    //QR 생성하는곳
    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String assetCode) {
        try {
            // 자산 코드에 기반하여 QR 코드를 생성합니다.
            String url = "http://localhost:8080/" + assetCode;
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

//    //QR 조회
//    @GetMapping("/{assetCode}")
//    public ResponseEntity<String> getQRCode(@PathVariable("assetCode") String assetCode) {
//
//    }

    private final AssetService assetService;

    @GetMapping("/assets/approved-not-disposed")
    public List<CommonAsset> getApprovedAndNotDisposedAssets() {
        return assetService.getApprovedAndNotDisposedAssets();
    }

    @GetMapping("/assets/{assetCode}")
    public Optional<CommonAsset> getCommonAsset(@PathVariable("assetCode") String assetCode) {
        return assetService.getCommonAsset(assetCode);
    }

    @GetMapping("/asset/{assetCode}")
    public AssetDto getAssetDetail(@PathVariable("assetCode") String assetCode) {
        return assetService.getAssetDetail(assetCode);
    }

}
