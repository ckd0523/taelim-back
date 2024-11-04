package com.codehows.taelim.service;

import com.codehows.taelim.entity.CommonAsset;

import com.codehows.taelim.godex.EZioLib;
import com.codehows.taelim.godex.GodexPrinter;
import com.codehows.taelim.godex.clsPrinterCommand;
import com.codehows.taelim.godex.clsPrinterConfig;
import com.codehows.taelim.repository.CommonAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@Service
public class QRService {

    @Value("${qr.url}")
    private String QRurl;

    //EZioLib.API API = EZioLib.API.INSTANCE;
    private final EZioLib.API API;

    @Autowired
    public QRService(CommonAssetRepository commonAssetRepository, UserService userService) {

        this.commonAssetRepository = commonAssetRepository;
        this.userService = userService;
        this.API = EZioLib.API.INSTANCE;
        // ... (나머지 초기화 코드)
    }



    private final CommonAssetRepository commonAssetRepository;
    private final UserService userService;


    // 네트워크로 프린터 연결
    public void Open(String strIP, String strPort) {
        API.OpenNet(strIP, strPort);
    }

    // 프린터 포트 닫기
    public void Close() {
        API.closeport();
    }



    public int PrintText_Unicode(int PosX, int PosY, int FontHeight, String FontName, String Data)
    {
        byte[] byteData = null;
        try {
            byteData = Data.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return API.ecTextOutW(PosX, PosY, FontHeight, FontName, byteData,Data.length());
    }

    // 자산 라벨 출력
    public void PrintAssetLabel(Long assetNo) {
        CommonAsset commonAsset = commonAssetRepository.findById(assetNo).orElseThrow();
        String url1 = QRurl + commonAsset.getAssetCode();
        String manager = userService.getUserById(commonAsset.getAssetSecurityManager()).getFullname();

        // 프린터 열기
        API.setup(60, 10, 3, 2, 5, 5);
        Open("172.20.20.137","9100");
        API.sendcommand("^L"); // 시작 명령
        PrintText_Unicode(25, 50, 30, "Hy중고딕", "자산명 : ");
        PrintText_Unicode(175, 50, 30, "Hy중고딕", commonAsset.getAssetName());
        PrintText_Unicode(25, 100, 30, "Hy중고딕", "자산코드 : ");
        PrintText_Unicode(175, 100, 30, "Hy중고딕", commonAsset.getAssetCode());
        PrintText_Unicode(25, 150, 30, "Hy중고딕", "관리자 : ");
        PrintText_Unicode(175, 150, 30, "Hy중고딕", manager);
        PrintText_Unicode(80, 230, 24, "Hy중고딕", "이 자산은 태림산업(주)의 소중한 자산입니다.");
        API.Bar_QRcode_S(475, 20, 50, url1);
        // 자산명 출력
        API.sendcommand("E"); // 시작 명령
        // 프린터 닫기
        Close();

    }

}
