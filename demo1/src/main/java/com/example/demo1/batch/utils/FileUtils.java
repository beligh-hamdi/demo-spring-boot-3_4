package com.example.demo1.batch.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.example.demo1.batch.model.Line;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FileUtils {

    private String fileName;
    private CSVReader CSVReader;
    private CSVWriter CSVWriter;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private File file;

    public FileUtils(String fileName) {
        this.fileName = fileName;
    }

    public Line readLine() {
        try {
            if (CSVReader == null) initReader();
            String[] line = CSVReader.readNext();
            if (line == null) return null;
            return new Line(line[0], LocalDate.parse(line[1], DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        } catch (Exception e) {
            log.error("Error while reading line in file: {}", this.fileName);
            return null;
        }
    }

    public void writeLine(Line line) {
        try {
            if (CSVWriter == null) initWriter();
            String[] lineStr = new String[2];
            lineStr[0] = line.getName();
            lineStr[1] = line
              .getAge()
              .toString();
            CSVWriter.writeNext(lineStr);
        } catch (Exception e) {
            log.error("Error while writing line in file: " + this.fileName);
        }
    }

    private void initReader() throws Exception {
        ClassLoader classLoader = this
          .getClass()
          .getClassLoader();
        if (file == null) file = new File(classLoader
          .getResource(fileName)
          .getFile());
        if (fileReader == null) fileReader = new FileReader(file);
        if (CSVReader == null) CSVReader = new CSVReader(fileReader);
    }

    private void initWriter() throws Exception {
        if (file == null) {
            file = new File(fileName);
            file.createNewFile();
        }
        if (fileWriter == null) fileWriter = new FileWriter(file, true);
        if (CSVWriter == null) CSVWriter = new CSVWriter(fileWriter);
    }

    public void closeWriter() {
        try {
            CSVWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Error while closing writer.");
        }
    }

    public void closeReader() {
        try {
            CSVReader.close();
            fileReader.close();
        } catch (IOException e) {
            log.error("Error while closing reader.");
        }
    }

}
