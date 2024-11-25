package com.example.demo.product;


import static java.math.BigDecimal.TEN;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import com.example.demo.product.kafka.ProductPriceChangedEvent;
import com.example.demo.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
//@TestPropertySource(
//        properties = {
////                "spring.kafka.consumer.auto-offset-reset=earliest",
////                "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
////                "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
////                "spring.kafka.consumer.group-id=demo",
////                "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
////                "spring.kafka.consumer.properties.spring.json.trusted.packages=com.example.demo.*",
////                "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer"
//        }
//)
@Testcontainers
class ProductPriceChangedEventHandlerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, ProductPriceChangedEvent> kafkaTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        var product = new Product(null, "P100", "Product One", TEN);
        productRepository.save(product);
    }

    @AfterAll
    static void tearDown() {
        mongoDBContainer.stop();
        kafka.stop();
    }

    @Test
    void shouldHandleProductPriceChangedEvent() {
        var event = new ProductPriceChangedEvent("P100", new BigDecimal("14.50"));

        kafkaTemplate.send("product-price-changes", event.code(), event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<Product> optionalProduct = productRepository.findByCode("P100");
                    assertThat(optionalProduct).isPresent();
                    assertThat(optionalProduct.get().code()).isEqualTo("P100");
                    assertThat(optionalProduct.get().price()).isEqualTo(new BigDecimal("14.50"));
                });
    }
}
