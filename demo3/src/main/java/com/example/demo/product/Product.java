package com.example.demo.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
public record Product(@Id String id, String code, String name, BigDecimal price) {
}
