package com.codehows.taelim.service;

import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import com.codehows.taelim.secondRepository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginTestService {

    private final TestMemberRepository testMemberRepository;
    private final AspNetUserRepository aspNetUserRepository;

    public String getOriginalName(String email) {
        return testMemberRepository.findUNameByEmail(email);
    }

    public AspNetUser getUser(String email) {
        System.out.println("로그인 테스트 서비스1");
        AspNetUser user = aspNetUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return user;
    }
}
