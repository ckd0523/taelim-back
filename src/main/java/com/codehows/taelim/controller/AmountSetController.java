package com.codehows.taelim.controller;

import com.codehows.taelim.dto.AmountSetDto;
import com.codehows.taelim.service.AmountSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AmountSetController {

    private final AmountSetService amountSetService;

    @GetMapping("/getAmountSet")
    public AmountSetDto getAmountSet() {
        return amountSetService.getAmountSet();
    }
}
