package com.codehows.taelim.config;

import com.codehows.taelim.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class DatabaseBackupScheduler { //백업 스크립트를 실행할 스케줄러 메서드 정의
    private final BackupService backupService;

    //@Scheduled(cron = "0 0 2 * * ?") // 매일 오전 2시에 작업 실행
    //@Scheduled(cron = "0 * * * * ?") // 1분 마다 작업 실행
    //@Scheduled(cron = "0 */5 * * * ?") // 5분마다 작업 실행
    @Scheduled(cron = "0 0 10 * * ?") //매일 오전 10시에 작업 실행
    public void backupDatabase() {
        try {
            // 백업 스크립트 경로를 지정
            //배시셸을 사용해서, 이 경로에 있는 백업 셸 실행
            String[] command = {"cmd.exe", "/c", "C:\\springbootwork\\assetBackUpScript.bat"};

            // 프로세스를 시작
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // 프로세스의 출력 결과를 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // 프로세스가 종료될 때까지 기다림
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                //배치파일 실행 성공 시 DB에 이력 추가
                backupService.backup();

                System.out.println("Database backup completed successfully.");
            } else {
                System.err.println("Database backup failed with exit code " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("백업 실패");
            //e.printStackTrace();
        }
    }
}
