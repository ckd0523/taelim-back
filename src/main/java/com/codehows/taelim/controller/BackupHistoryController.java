package com.codehows.taelim.controller;

import com.codehows.taelim.entity.BackUpHistory;
import com.codehows.taelim.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BackupHistoryController {

    private final BackupService backupService;

    //백업 이력 조회
    @GetMapping("/backUpHistory")
    public List<BackUpHistory> getAllBackUpHistory(){
        return backupService.getAllBackUpHistory();
    }
}
