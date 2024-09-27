package com.codehows.taelim.service;

import com.codehows.taelim.dto.DemandHistoryDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.entity.DemandDtl;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.DemandDtlRepository;
import com.codehows.taelim.repository.DemandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DemandService {

    private final DemandDtlRepository demandDtlRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final DemandRepository demandRepository;

    public List<DemandHistoryDto> getAllDemandHistory() {

        List<DemandDtl> demandDtls = demandDtlRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()){
                Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                Demand demand1 = demand.orElseThrow();
                if(demand1.getDisposeLocation() == null){
                    //수정이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("update");
                }else {
                    //폐기이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("delete");
                }
                demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                demandHistoryDto.setAssetNo(asset.getAssetNo());
                demandHistoryDto.setAssetCode(asset.getAssetCode());
                demandHistoryDto.setDemandDate(asset.getCreateDate());
                demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                demandHistoryDtos.add(demandHistoryDto);
            }

        }

        return demandHistoryDtos;

    }
}
