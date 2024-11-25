package com.example.demo.batch.config;

import com.example.demo.batch.mapper.CsvRecordSetMapper;
import com.example.demo.batch.model.CsvRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfiguration {

    private final MongoTemplate mongoTemplate;

    @Value("${application.batch.file-input}")
    private String inputFilePath;

    @Value("${application.batch.output-collection-name}")
    private String outputCollectionName;

    @Bean
    public FlatFileItemReader<CsvRecord> csvItemReader() {

        log.info("Reading csv records from {}", inputFilePath);

        var names = new String[]{"title", "description"};

        return new FlatFileItemReaderBuilder<CsvRecord>()
                .name("csvReader")
                .resource(new ClassPathResource(inputFilePath))
                .delimited()
                .names(names)
                .linesToSkip(1)
                .fieldSetMapper(new CsvRecordSetMapper())
                .build();
    }

    @Bean
    public ItemProcessor<CsvRecord, CsvRecord> csvItemProcessor() {
        return item -> {
            // Process the item here, e.g., transform data
            return item;
        };
    }

    @Bean
    public MongoItemWriter<CsvRecord> csvItemWriter() {

        log.info("Writing csv records to mongo {}", outputCollectionName);
        return new MongoItemWriterBuilder<CsvRecord>()
                .template(mongoTemplate)
                .collection(outputCollectionName)
                .build();
    }
}
