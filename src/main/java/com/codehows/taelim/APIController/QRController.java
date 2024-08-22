package com.codehows.taelim.APIController;

import com.codehows.taelim.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;


import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class QRController {

    private final QRService qrCodeService;

    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String assetCode) {
        try {
            String url = "http://localhost:8080/" + assetCode;
            byte[] qrCode = qrCodeService.generateQRCode(url, 200, 200);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return ResponseEntity.ok().headers(headers).body(qrCode);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
