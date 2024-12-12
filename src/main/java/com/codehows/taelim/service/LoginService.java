package com.codehows.taelim.service;

import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AspNetUserRepository aspNetUserRepository;

    public AspNetUser getOriginalName(String email) {
        return aspNetUserRepository.findFullNameByEmail(email);
    }
}
