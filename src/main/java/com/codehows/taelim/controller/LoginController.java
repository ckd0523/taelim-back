package com.codehows.taelim.controller;

import com.codehows.taelim.service.LoginTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private LoginTestService loginTestService;

    @PostMapping("login")
    public String checkLogin() {
        return "text";
    }
}
