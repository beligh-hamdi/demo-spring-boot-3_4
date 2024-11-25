package com.example.demo.product.repository;


import java.math.BigDecimal;

public interface ProductRepositoryCustom {
    void updateCodePrice(String code, BigDecimal price);
}
