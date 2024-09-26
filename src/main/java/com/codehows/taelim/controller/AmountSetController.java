package com.codehows.taelim.controller;

import com.codehows.taelim.dto.AmountSetDto;
import com.codehows.taelim.entity.AmountSet;
import com.codehows.taelim.service.AmountSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AmountSetController {

    private final AmountSetService amountSetService;

    @GetMapping("/getAmountSet")
    public AmountSetDto getAmountSet() {
        return amountSetService.getAmountSet();
    }

    @PostMapping("/changeAmountSet")
    public ResponseEntity<Void> changeAmountSet(@RequestBody AmountSetDto amountSetDto) {
        AmountSet amountSet = new AmountSet();
        amountSet.setHighValueStandard(amountSetDto.getHigh_value_standard());
        amountSet.setLowValueStandard(amountSetDto.getLow_value_standard());

        boolean result = amountSetService.changeAmountSet(amountSet);

        if(result) {
            System.out.println("11111111111111111");
            return ResponseEntity.ok().build();
        }

        System.out.println("22222222222222222");
        return ResponseEntity.badRequest().build();
    }
}
