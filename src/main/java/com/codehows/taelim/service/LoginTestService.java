package com.codehows.taelim.service;

import com.codehows.taelim.loginRepository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginTestService {

    private final TestMemberRepository testMemberRepository;


}
