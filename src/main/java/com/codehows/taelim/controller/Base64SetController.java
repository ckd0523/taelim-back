package com.codehows.taelim.controller;

import com.codehows.taelim.dto.Base64SetDto;
import com.codehows.taelim.entity.Base64Set;
import com.codehows.taelim.service.Base64SetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/base64Set")
public class Base64SetController {

    private final Base64SetService base64SetService;

    // 현재 설정 값 조회
    @GetMapping("/findSet")
    public Base64Set getBase64Config() {
        return base64SetService.getBase64Config();
    }

    @PutMapping("/updateSet")
    public Base64Set updateBase64Config() {
        return base64SetService.updateBase64Config();
    }
}
