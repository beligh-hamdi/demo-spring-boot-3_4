package com.example.demo.customer;

import org.springframework.data.annotation.Id;

public record Customer(@Id String id, String firstName, String lastName) {
}
