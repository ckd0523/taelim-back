package com.codehows.taelim.service;

import com.codehows.taelim.entity.CommonAsset;

import com.codehows.taelim.godex.GodexPrinter;
import com.codehows.taelim.repository.CommonAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class QRService {

    @Value("${qr.url}")
    private String QRurl;

    private final GodexPrinter printer; // 주입받도록 변경

    private final CommonAssetRepository commonAssetRepository;

    // 네트워크로 프린터 연결
    public void Open(String strIP, String strPort) {
        printer.Open(strIP, strPort);
    }

    // 프린터 포트 닫기
    public void Close() {
        printer.Close();
    }

    // QR 코드 출력
    public void PrintQRCode(String url, int posX, int posY) {
        printer.Command.Start(); // 시작 명령
        printer.Command.PrintQRCode(posX, posY, 2, 7, "L", 0, 1, url.length(), url); // QR 코드 출력
        printer.Command.End(); // 완료 명령
    }

    // 텍스트 출력
    public void PrintText(String text, int posX, int posY, int fontSize) {
        printer.Command.PrintText(posX, posY, fontSize, "Arial", text); // 텍스트 출력
    }

    // 자산 라벨 출력
    public void PrintAssetLabel(Long assetNo) {
        CommonAsset commonAsset = commonAssetRepository.findById(assetNo).orElseThrow();
        String url = QRurl + commonAsset.getAssetCode();
        //String url = "http://125.6.38.5:5173/jsx/" + commonAsset.getAssetCode();
        // 프린터 열기
        Open("","");

        // 자산명 출력
        PrintText("자산명", 50, 50, 24);
        PrintText(commonAsset.getAssetName(), 200, 50, 24);

        // 자산코드 출력
        PrintText("자산코드", 50, 100, 24);
        PrintText(commonAsset.getAssetCode(), 200, 100, 24);

        // 담당자 출력
        //PrintText("관리자", 50, 150, 24);
        //PrintText(manager, 200, 150, 24);

        // QR 코드 출력
        PrintQRCode(url, 400, 50);

        // 하단 설명 문구 출력
        PrintText("이 자산은 태림산업(주)의 소중한 자산입니다", 50, 250, 20);

        // 프린터 닫기
        Close();

    }

}
