package com.codehows.taelim.service;

import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.repository.RepairHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairHistoryRepository repairHistoryRepository;

    //유지보수 등록
    public Long repairRegister(RepairDto repairDto) {


        RepairHistory repairHistory = repairDto.toRepairHistory();


        Long assetNo = repairDto.toAssetNo().getAssetNo();
        System.out.println("assetNo" + assetNo);
        repairDto.setAssetNo(assetNo);
        repairHistoryRepository.save(repairHistory);

        return assetNo;

    }

    //유지보수 조회
    public List<RepairDto> findAll(){

        return repairHistoryRepository.findAll().stream().map(RepairHistory::toDto).collect(Collectors.toList());
    }

    public Optional<RepairHistory> findById(Long id) {
        return repairHistoryRepository.findById(id);
    }
}
