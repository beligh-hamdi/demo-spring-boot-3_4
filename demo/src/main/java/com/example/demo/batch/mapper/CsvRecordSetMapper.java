package com.example.demo.batch.mapper;

import com.example.demo.batch.model.CsvRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CsvRecordSetMapper implements FieldSetMapper<CsvRecord> {

    @Override
    public CsvRecord mapFieldSet(FieldSet fieldSet) throws BindException {
        return new CsvRecord(
                fieldSet.readString("title"),
                fieldSet.readString("description"));
    }
}
