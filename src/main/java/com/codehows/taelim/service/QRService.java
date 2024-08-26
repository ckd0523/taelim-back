package com.codehows.taelim.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class QRService {

    //QR 생성하는 서비스
    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
        // QRCodeWriter 인스턴스를 생성하여 QR 코드를 생성할 준비를 합니다.
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // 주어진 텍스트를 바탕으로 QR 코드를 인코딩합니다.
        // BarcodeFormat.QR_CODE는 QR 코드 형식을 의미합니다.
        // width와 height는 QR 코드의 가로 및 세로 크기를 지정합니다.
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // QR 코드 비트 매트릭스를 PNG 형식으로 변환하기 위해 ByteArrayOutputStream을 사용합니다.
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        // MatrixToImageWriter 클래스를 사용하여 비트 매트릭스를 PNG 이미지로 변환하고
        // ByteArrayOutputStream에 작성합니다.
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // ByteArrayOutputStream에서 바이트 배열을 추출하여 반환합니다.
        // 이 바이트 배열은 PNG 형식의 QR 코드 이미지 데이터를 포함합니다.
        return pngOutputStream.toByteArray();
    }


}
