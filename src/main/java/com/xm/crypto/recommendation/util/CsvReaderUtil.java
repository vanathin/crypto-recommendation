package com.xm.crypto.recommendation.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvReaderUtil {

    public static List<CSVRecord> readCsvFile(String filePath) throws IOException {
        Reader reader = new InputStreamReader(new ClassPathResource(filePath).getInputStream(), StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = csvParser.getRecords();
        csvParser.close();
        reader.close();
        return records;
    }
}
