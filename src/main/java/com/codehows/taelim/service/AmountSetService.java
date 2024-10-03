package com.codehows.taelim.service;

import com.codehows.taelim.dto.AmountSetDto;
import com.codehows.taelim.entity.AmountSet;
import com.codehows.taelim.repository.AmountSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmountSetService {
    private final AmountSetRepository amountSetRepository;

    public AmountSetDto getAmountSet() {
        AmountSet amountSet = amountSetRepository.findAmountSetByValueStandardNo(1L);
        return new AmountSetDto(amountSet.getHighValueStandard(), amountSet.getLowValueStandard());
    }

    public boolean changeAmountSet(AmountSet amountSet) {
        try {
            amountSetRepository.updateAmountSet(amountSet.getHighValueStandard(), amountSet.getLowValueStandard());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
