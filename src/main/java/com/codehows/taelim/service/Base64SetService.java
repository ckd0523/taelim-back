package com.codehows.taelim.service;

import com.codehows.taelim.entity.Base64Set;
import com.codehows.taelim.repository.Base64SetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class Base64SetService {


    private final Base64SetRepository base64SetRepository;

    // 설정 값 조회 (첫 번째 설정 가져오기)
    public Base64Set getBase64Config() {
        // 여기가 올바른 값을 가져오도록 구현되어 있는지 확인
        return base64SetRepository.findById(1L).orElseThrow(() -> new RuntimeException("Config not found"));
    }

    // 설정 값 업데이트
    @Transactional
    public Base64Set updateBase64Config() {
        Base64Set config = getBase64Config();
        if(config.isEnabled()) {
            config.setEnabled(false);
            base64SetRepository.save(config);
        }else{
            config.setEnabled(true);
            base64SetRepository.save(config);
        }
        return config;
    }
}
