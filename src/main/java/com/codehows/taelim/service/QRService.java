package com.codehows.taelim.service;

import com.codehows.taelim.entity.CommonAsset;

import com.codehows.taelim.godex.EZioLib;
import com.codehows.taelim.godex.GodexPrinter;
import com.codehows.taelim.godex.clsPrinterCommand;
import com.codehows.taelim.godex.clsPrinterConfig;
import com.codehows.taelim.repository.CommonAssetRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class QRService {

////    EZioLib.API API = EZioLib.API.INSTANCE;
////    clsPrinterConfig Config = new clsPrinterConfig();
////    clsPrinterCommand Command = new clsPrinterCommand();
//
//
//    private final CommonAssetRepository commonAssetRepository;
//
//
//
//    // 프린터 연결 (직렬 포트 또는 드라이버)
//    public void Open(String PortName) {
//        if (PortName.contains("COM")) {
//            API.OpenUSB(PortName);
//        } else {
//            API.OpenDriver(PortName);
//        }
//    }
//
//    // 네트워크로 프린터 연결
//    public void Open(String strIP, String strPort) {
//        API.OpenNet(strIP, strPort);
//    }
//
//    // 프린터 포트 닫기
//    public void Close() {
//        API.closeport();
//    }
//
//    // DLL 버전 정보 가져오기
//    public String GetVersion() {
//        byte[] ByteData = new byte[50];
//        API.GetDllVersion(ByteData);
//        String strData = new String(ByteData).trim();
//        return strData;
//    }
//
//    // QR 코드 출력
//    public void PrintQRCode(String url, int posX, int posY) {
//        API.sendcommand("^L"); // 시작 명령
//        API.sendcommand(String.format("BQR %d,%d,Q,2,7,H,0,M2,S7,\"%s\"", posX, posY, url));
//        API.sendcommand("E"); // 완료 명령
//    }
//
//    // 텍스트 출력
//    public void PrintText(String text, int posX, int posY, int fontSize) {
//        API.sendcommand(String.format("T %d,%d,%d,0,1,\"%s\"", posX, posY, fontSize, text));
//    }
//
//    // 자산 라벨 출력
//    public void PrintAssetLabel(Long assetNo) {
//        CommonAsset commonAsset = commonAssetRepository.findById(assetNo).orElseThrow();
//        String url = "http://localhost:5173/asset1/" + commonAsset.getAssetCode();
//        //String url = "http://125.6.38.5:5173/jsx/" + commonAsset.getAssetCode();
//        // 프린터 열기
//        Open("","");
//
//        // 자산명 출력
//        PrintText("자산명", 50, 50, 24);
//        PrintText(commonAsset.getAssetName(), 200, 50, 24);
//
//        // 자산코드 출력
//        PrintText("자산코드", 50, 100, 24);
//        PrintText(commonAsset.getAssetCode(), 200, 100, 24);
//
//        // 담당자 출력
//        //PrintText("관리자", 50, 150, 24);
//        //PrintText(manager, 200, 150, 24);
//
//        // QR 코드 출력
//        PrintQRCode(url, 400, 50);
//
//        // 하단 설명 문구 출력
//        PrintText("이 자산은 태림산업(주)의 소중한 자산입니다", 50, 250, 20);
//
//        // 프린터 닫기
////        Close();
//
//    }

}