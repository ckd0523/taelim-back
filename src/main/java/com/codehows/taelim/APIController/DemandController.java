package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.DemandHistoryDto;
import com.codehows.taelim.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DemandController {

    private final DemandService demandService;

    //요청 리스트 가져오기
    @GetMapping("/DemandHistory")
    public List<DemandHistoryDto> getDemandHistory() {
        return demandService.getAllDemandHistory();
    }

}
