package com.codehows.taelim;

import com.codehows.taelim.service.DataInitializerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataIntitializerRunner implements CommandLineRunner {

    private final DataInitializerService dataInitializerService;

    @Override
    public void run(String... args) throws Exception {
        dataInitializerService.insertDummyData();
    }

}
