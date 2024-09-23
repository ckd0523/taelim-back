package com.codehows.taelim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling //스케줄링 기능 활성화
public class SchedulerConfig { // 스케줄러를 이용하여 자동 백업 기능 구현하기 위해 스케줄링 기능 활성화
}
