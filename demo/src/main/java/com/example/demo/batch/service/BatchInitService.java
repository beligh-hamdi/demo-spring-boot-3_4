package com.example.demo.batch.service;

import com.mongodb.MongoCommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchInitService implements ApplicationRunner {

    private  final MongoTemplate mongoTemplate;

    private static final String BATCH_JOB_INSTANCE = "BATCH_JOB_INSTANCE";
    private static final String BATCH_JOB_EXECUTION = "BATCH_JOB_EXECUTION";
    private static final String BATCH_STEP_EXECUTION = "BATCH_STEP_EXECUTION";
    private static final String BATCH_SEQUENCES = "BATCH_SEQUENCES";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            mongoTemplate.dropCollection(BATCH_JOB_INSTANCE);
            mongoTemplate.dropCollection(BATCH_JOB_EXECUTION);
            mongoTemplate.dropCollection(BATCH_STEP_EXECUTION);
            mongoTemplate.dropCollection(BATCH_SEQUENCES);

            log.info("creating meta-data collections and sequences for batch");
            mongoTemplate.createCollection(BATCH_JOB_INSTANCE);
            mongoTemplate.createCollection(BATCH_JOB_EXECUTION);

            mongoTemplate.createCollection(BATCH_STEP_EXECUTION);
            mongoTemplate.createCollection(BATCH_SEQUENCES);

            mongoTemplate.getCollection(BATCH_SEQUENCES)
                    .insertOne(new Document(Map.of("_id", "BATCH_JOB_INSTANCE_SEQ", "count", 0L)));
            mongoTemplate.getCollection(BATCH_SEQUENCES)
                    .insertOne(new Document(Map.of("_id", "BATCH_JOB_EXECUTION_SEQ", "count", 0L)));
            mongoTemplate.getCollection(BATCH_SEQUENCES)
                    .insertOne(new Document(Map.of("_id", "BATCH_STEP_EXECUTION_SEQ", "count", 0L)));
        } catch (Exception e) {
            log.warn("Try to create meta-data collections and sequences for batch: {}", e.getMessage());
        }
    }
}
