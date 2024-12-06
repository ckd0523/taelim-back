package com.codehows.taelim.config;

import com.codehows.taelim.entity.AmountSet;
import com.codehows.taelim.entity.Base64Set;
import com.codehows.taelim.repository.AmountSetRepository;
import com.codehows.taelim.repository.Base64SetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    @Bean
    CommandLineRunner initDatabase(Base64SetRepository base64SetRepository, AmountSetRepository amountSetRepository) {
        return args -> {
            if (base64SetRepository.count() == 0) {
                base64SetRepository.save(new Base64Set(null, false)); // 기본 설정값
            }
            if (amountSetRepository.count() == 0) {
                amountSetRepository.save(new AmountSet(null, 1L, 1L));
            }
        };
    }
}
