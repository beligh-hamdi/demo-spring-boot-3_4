package com.example.demo.product.repository;

import com.example.demo.product.Product;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateCodePrice(String code, BigDecimal price) {
        var query = new Query(Criteria.where("code").is(code));
        Update update = new Update().set("price", price);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Product.class);
        log.info("updateResult: {}", updateResult);
    }
}
