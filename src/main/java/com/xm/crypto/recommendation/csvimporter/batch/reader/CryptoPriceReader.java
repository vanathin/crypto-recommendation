package com.xm.crypto.recommendation.csvimporter.batch.reader;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
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

    private final CryptoPriceRepository cryptoPriceRepository;
    private Resource resource;

    public CryptoPriceReader(CryptoPriceRepository cryptoFileImportRepository) {
        this.cryptoPriceRepository = cryptoFileImportRepository;
        setLineMapper(createCryptoPriceLineMapper());
        setLinesToSkip(1); // skip header line
    }
    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }
    @Override
    protected CryptoFileImportDto doRead() throws Exception {
        CryptoFileImportDto item = super.doRead();
        String filename = resource.getFilename();
        ZonedDateTime lastModified = getLastModifiedDate();


        if(item != null ) {
            boolean allowed = cryptoPriceRepository.existsByPriceTimestampAndCryptoSymbol
                    (ZonedDateTime.ofInstant(Instant.ofEpochMilli(item.timestamp()), ZoneId.systemDefault()), item.symbol());

            if (!allowed) {
                return new CryptoFileImportDto(item.timestamp(), item.symbol(), item.price(), filename, lastModified);
            }
        }

        return null;
    }

    private ZonedDateTime getTimeStamp(Long timeStamp) throws IOException {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
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
