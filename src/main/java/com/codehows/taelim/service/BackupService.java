package com.codehows.taelim.service;

import com.codehows.taelim.constant.BackUpScope;
import com.codehows.taelim.entity.BackUpHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.codehows.taelim.repository.BackUpHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BackupService {

    private final BackUpHistoryRepository backUpHistoryRepository;

    //스케줄러에서 DB 백업을 할 때 백업 이력 등록
    public void backup() {
        BackUpHistory backUpHistory = new BackUpHistory();
        backUpHistory.setBackUpDate(LocalDate.now());
        backUpHistory.setBackUpScope(BackUpScope.FULL);

        backUpHistoryRepository.save(backUpHistory);

        System.out.println("Backup and record saving successful.");

    }

    //백업 이력 조회
    public List<BackUpHistory> getAllBackUpHistory() {
        return backUpHistoryRepository.findAll();
    }

    //검색은 프론트에서 할듯
    /*
    // 특정 기간의 백업 이력을 가져오는 메서드
    public List<BackUpHistory> getBackupHistoryInPeriod(LocalDate startDate, LocalDate endDate) {
        return backUpHistoryRepository.findByBackUpDateBetween(startDate, endDate);
    }

    // 특정 범위의 백업 이력을 가져오는 메서드
    public List<BackUpHistory> getBackupHistoryByScope(BackUpScope scope) {
        return backUpHistoryRepository.findByBackUpScope(scope);
    }
    */
}
