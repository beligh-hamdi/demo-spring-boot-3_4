package com.example.demo.batch.config;

import com.example.demo.batch.model.CsvRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

    private final ItemReader<CsvRecord> csvItemReader;
    private final ItemProcessor<CsvRecord, CsvRecord> csvItemProcessor;
    private final ItemWriter<CsvRecord> csvItemWriter;

   // ResourcelessJobRepository jobRepository

    @Bean
    Job importUserJob(Step step1, JobRepository jobRepository) {
//        return new JobBuilder("importUserJob",  jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .flow(step1)
//                .end()
//                .build();
        log.info("Job: importUserJob");
        return new JobBuilder("importUserJob",  jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("Step: step1");
        return new StepBuilder("step1", jobRepository)
                .<CsvRecord, CsvRecord> chunk(10, transactionManager)
                .reader(csvItemReader)
                .processor(csvItemProcessor)
                .writer(csvItemWriter)
                .build();
    }
}
