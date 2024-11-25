package com.example.mongodb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

public class InsertionJobConfiguration {

    @Bean
    public MongoPagingItemReader<Person> mongoItemReader(MongoTemplate mongoTemplate) {
        Map<String, Sort.Direction> sortOptions = new HashMap<>();
        sortOptions.put("name", Sort.Direction.DESC);
        return new MongoPagingItemReaderBuilder<Person>().name("personItemReader")
                .collection("person_in")
                .targetType(Person.class)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(sortOptions)
                .build();
    }

    @Bean
    public MongoItemWriter<Person> mongoItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Person>().template(mongoTemplate).collection("person_out").build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     MongoPagingItemReader<Person> mongoItemReader, MongoItemWriter<Person> mongoItemWriter) {
        return new StepBuilder("step", jobRepository).<Person, Person>chunk(2, transactionManager)
                .reader(mongoItemReader)
                .writer(mongoItemWriter)
                .build();
    }

    @Bean
    public Job insertionJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("insertionJob", jobRepository).start(step).build();
    }
}
