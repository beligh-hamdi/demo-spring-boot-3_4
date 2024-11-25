package com.example.demo.product.repository;


import com.example.demo.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {

    Optional<Product> findByCode(String code);

//    @Query("{'id' : ?0}")
//    @Update("{'$set': { 'code': ?1, 'price': ?2 } }")
//    void updateProductPrice(
//            @Param("id") String id,
//            @Param("code") String code,
//            @Param("price") BigDecimal price
//    );
//
   //  @Update("{'$set': { 'code': ?0, 'price': ?1 } }")
   // void updateCodePrice(String code, BigDecimal price);
}
