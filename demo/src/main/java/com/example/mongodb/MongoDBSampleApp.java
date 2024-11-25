package com.example.mongodb;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDBSampleApp {

    public static void main(String[] args) throws Exception {
        Class<?>[] configurationClasses = { InsertionJobConfiguration.class, DeletionJobConfiguration.class,
                MongoDBConfiguration.class };
        ApplicationContext context = new AnnotationConfigApplicationContext(configurationClasses);
        MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);

        try {
            // create meta-data collections and sequences
            mongoTemplate.createCollection("BATCH_JOB_INSTANCE");
            mongoTemplate.createCollection("BATCH_JOB_EXECUTION");
            mongoTemplate.createCollection("BATCH_STEP_EXECUTION");
            mongoTemplate.createCollection("BATCH_SEQUENCES");
            mongoTemplate.getCollection("BATCH_SEQUENCES")
                    .insertOne(new Document(Map.of("_id", "BATCH_JOB_INSTANCE_SEQ", "count", 0L)));
            mongoTemplate.getCollection("BATCH_SEQUENCES")
                    .insertOne(new Document(Map.of("_id", "BATCH_JOB_EXECUTION_SEQ", "count", 0L)));
            mongoTemplate.getCollection("BATCH_SEQUENCES")
                    .insertOne(new Document(Map.of("_id", "BATCH_STEP_EXECUTION_SEQ", "count", 0L)));
        } catch (Exception e) {

        }


        // clear collections and insert some documents in "person_in"
        MongoCollection<Document> personsIn = mongoTemplate.getCollection("person_in");
        MongoCollection<Document> personsOut = mongoTemplate.getCollection("person_out");
        personsIn.deleteMany(new Document());
       // personsOut.deleteMany(new Document());
        personsIn.insertMany(Arrays.asList(new Document("name", "foo1"), new Document("name", "foo2"),
                new Document("name", "foo3"), new Document("name", "foo4")));

        // run the insertion job
        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job insertionJob = context.getBean("insertionJob", Job.class);
        jobLauncher.run(insertionJob, new JobParameters());

        // check results
        List<Person> persons = mongoTemplate.findAll(Person.class, "person_out");
        System.out.println("Checking persons in person_out collection");
        for (Person person : persons) {
            System.out.println(person);
        }

        // run the deletion job
        Job deletionJob = context.getBean("deletionJob", Job.class);
        jobLauncher.run(deletionJob, new JobParameters());

        // check results (foo3 should have been removed)
        persons = mongoTemplate.findAll(Person.class, "person_out");
        System.out.println("Checking persons in person_out collection after deleting foo3");
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}
