package com.example.demo.product.kafka;


import java.math.BigDecimal;

public record ProductPriceChangedEvent(String code, BigDecimal price) {}
