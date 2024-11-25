package com.example.demo.product.kafka;

import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
@Transactional
class ProductPriceChangedEventHandler {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "product-price-changes", groupId = "demo")
    public void handle(@Payload ProductPriceChangedEvent event) {
        log.info("Received a ProductPriceChangedEvent with productCode:{}: ", event.code());
        productRepository.updateCodePrice(event.code(), event.price());
    }
}
