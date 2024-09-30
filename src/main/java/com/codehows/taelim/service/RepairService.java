package com.codehows.taelim.service;

import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.dto.RepairFileDto;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.repository.RepairFileRepository;
import com.codehows.taelim.repository.RepairHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairHistoryRepository repairHistoryRepository;
    private final RepairFileRepository repairFileRepository;

    //유지보수 등록
    public Long repairRegister(RepairDto repairDto) {


        RepairHistory repairHistory = repairDto.toRepairHistory();

//        repairDto.setAssetNo(repairHistory.getAssetNo().getAssetNo());
//        Long repairNo = repairHistory.getRepairNo();
//        repairDto.setRepairNo(repairNo);
        repairHistoryRepository.save(repairHistory);

        System.out.println("repairNum:" + repairHistory.getRepairNo());
        return repairHistory.getRepairNo();

    }

    //유지보수 조회
    public List<RepairDto> findAll(){

        List<RepairDto> repairHistories = repairHistoryRepository.findAll().stream().map(RepairHistory::toDto).collect(Collectors.toList());
        for(RepairDto repairHistory : repairHistories){
            RepairHistory repairHistory1 = repairHistoryRepository.findById(repairHistory.getRepairNo()).orElseThrow(null);
            List<RepairFile> repairFiles = repairFileRepository.findByRepairNo(repairHistory1);
            List<RepairFileDto> repairFileDto = repairFiles.stream().map(RepairFile :: toRepairFile).collect(Collectors.toList());
            repairHistory.setRepairFiles(repairFileDto);
        }
        return repairHistories;
    }

    public Optional<RepairHistory> findById(Long id) {
        return repairHistoryRepository.findById(id);
    }
}
