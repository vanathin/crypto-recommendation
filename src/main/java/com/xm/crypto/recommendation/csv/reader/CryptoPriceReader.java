package com.xm.crypto.recommendation.csv.reader;

import com.xm.crypto.recommendation.csv.dto.CryptoFileImportDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class CryptoPriceReader extends FlatFileItemReader<CryptoFileImportDto> {


    public CryptoPriceReader() {
        setLineMapper(createCryptoPriceLineMapper());
        setLinesToSkip(1); // skip header line
    }

    private Resource resource;


    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }

    @Override
    protected CryptoFileImportDto doRead() throws Exception {
        CryptoFileImportDto item = super.doRead();

        if (item != null) {
            String filename = resource.getFilename();
            ZonedDateTime lastModified = getLastModifiedDate();

            // Inject additional details into the item.
            return new CryptoFileImportDto(item.timestamp(), item.symbol(), item.price(), filename, lastModified);
        }

        return null;
    }

    private ZonedDateTime getLastModifiedDate() throws IOException {
        long millis = Files.getLastModifiedTime(Paths.get(resource.getURI())).toMillis();
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    private LineMapper<CryptoFileImportDto> createCryptoPriceLineMapper() {
        DefaultLineMapper<CryptoFileImportDto> cryptoPriceMapper = new DefaultLineMapper<>();

        LineTokenizer tokenizer = createCryptoPriceLineTokenizer();
        cryptoPriceMapper.setLineTokenizer(tokenizer);

        FieldSetMapper<CryptoFileImportDto> priceInformationMapper = createCryptoPriceInformationMapper();
        cryptoPriceMapper.setFieldSetMapper(priceInformationMapper);

        return cryptoPriceMapper;
    }

    private LineTokenizer createCryptoPriceLineTokenizer() {
        DelimitedLineTokenizer cryptoPriceLineTokenizer = new DelimitedLineTokenizer();
        cryptoPriceLineTokenizer.setDelimiter(",");
        cryptoPriceLineTokenizer.setNames(new String[]{"timestamp", "symbol", "price"});
        return cryptoPriceLineTokenizer;
    }

    private FieldSetMapper<CryptoFileImportDto> createCryptoPriceInformationMapper() {
        return fieldSet -> new CryptoFileImportDto(
                fieldSet.readLong("timestamp"),
                fieldSet.readString("symbol"),
                fieldSet.readBigDecimal("price"),
                null,
                null

        );
    }

}
