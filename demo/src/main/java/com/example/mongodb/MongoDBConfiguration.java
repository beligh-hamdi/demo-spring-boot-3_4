package com.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MongoJobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MongoJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
@PropertySource("classpath:mongodb-sample.properties")
@EnableBatchProcessing
public class MongoDBConfiguration {

    @Value("${mongodb.host}")
    private String mongodbHost;

    @Value("${mongodb.port}")
    private int mongodbPort;

    @Value("${mongodb.database}")
    private String mongodbDatabase;

    @Bean
    public MongoClient mongoClient() {
        String connectionString = "mongodb://" + this.mongodbHost + ":" + this.mongodbPort + "/" + this.mongodbDatabase;
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "test");
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        converter.setMapKeyDotReplacement(".");
        return mongoTemplate;
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "test");
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    @Bean
    public JobRepository jobRepository(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager)
            throws Exception {
        MongoJobRepositoryFactoryBean jobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setMongoOperations(mongoTemplate);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public JobExplorer jobExplorer(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager)
            throws Exception {
        MongoJobExplorerFactoryBean jobExplorerFactoryBean = new MongoJobExplorerFactoryBean();
        jobExplorerFactoryBean.setMongoOperations(mongoTemplate);
        jobExplorerFactoryBean.setTransactionManager(transactionManager);
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }

}
