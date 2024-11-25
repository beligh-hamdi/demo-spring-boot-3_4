package com.example.demo.batch.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MongoJobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MongoJobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String mongodbDatabase;

    @Value("${spring.data.mongodb.uri}")
    private String mongodbUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbUri);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, this.mongodbDatabase);
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        converter.setMapKeyDotReplacement(".");
        return mongoTemplate;
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, this.mongodbDatabase);
    }

//    @Bean
//    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
//        return new MongoTransactionManager(mongoDatabaseFactory);
//    }


    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository() {
        return new ResourcelessJobRepository();
    }

    // https://github.com/spring-projects/spring-batch/commit/a297cd11f8bf2087143e0186d3f37933260a7cf1#diff-3189419a74c263ce1acdc83e2ba25c4f46a9637d09aff3764ca225600015cfca
    // @EnableBatchProcessing on main configuration class
    @Bean
    public JobExplorer jobExplorer(PlatformTransactionManager platformTransactionManager)  throws Exception{
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setTransactionManager(platformTransactionManager);
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }



//    @Bean
//    public JobRepository jobRepository(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager)
//            throws Exception {
//        MongoJobRepositoryFactoryBean jobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
//        jobRepositoryFactoryBean.setMongoOperations(mongoTemplate);
//        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
//        jobRepositoryFactoryBean.afterPropertiesSet();
//        return jobRepositoryFactoryBean.getObject();
//    }

//    @Bean
//    public JobExplorer jobExplorer(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager)
//            throws Exception {
//        MongoJobExplorerFactoryBean jobExplorerFactoryBean = new MongoJobExplorerFactoryBean();
//        jobExplorerFactoryBean.setMongoOperations(mongoTemplate);
//        jobExplorerFactoryBean.setTransactionManager(transactionManager);
//        jobExplorerFactoryBean.afterPropertiesSet();
//        return jobExplorerFactoryBean.getObject();
//    }

}