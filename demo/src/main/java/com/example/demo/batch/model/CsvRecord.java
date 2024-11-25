package com.example.demo.batch.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record CsvRecord(String title, String description) {
}
