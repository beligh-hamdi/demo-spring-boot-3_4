package com.example.demo.batch.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchRunConfiguration {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 10, timeUnit = TimeUnit.SECONDS )
    public void runJob() {
        log.info("Running job");
        try {
            jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            log.error("Error running job", e);
        }
    }
}
