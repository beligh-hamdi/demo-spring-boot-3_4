package com.example.demo.mongo;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MongoDbSpringIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void test() {
        DBObject objectToSave = BasicDBObjectBuilder.start().add("key", "value").get();

        mongoTemplate.save(objectToSave, "collection");

        assertThat(mongoTemplate.findAll(DBObject.class, "collection"))
                .extracting("key")
                .containsOnly("value");
    }
}

