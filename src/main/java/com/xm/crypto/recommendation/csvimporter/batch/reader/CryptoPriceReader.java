package com.xm.crypto.recommendation.csvimporter.batch.reader;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoPriceDTO;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
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
import java.time.*;

@Component
public class CryptoPriceReader extends FlatFileItemReader<CryptoPriceDTO> {

    private final CryptoFileImportRepository cryptoFileImportRepository;

    private final CryptoRepository cryptoRepository;
    private Resource resource;

    private final CryptoStatsReader cryptoStatsReader;


    public CryptoPriceReader(CryptoFileImportRepository cryptoFileImportRepository, CryptoRepository cryptoRepository,
                             CryptoStatsReader cryptoStatsReader) {
        this.cryptoFileImportRepository = cryptoFileImportRepository;
        this.cryptoRepository = cryptoRepository;
        this.cryptoStatsReader = cryptoStatsReader;
        setLineMapper(createCryptoPriceLineMapper());
        setLinesToSkip(1); // skip header line
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }

    @Override
    protected CryptoPriceDTO doRead() throws Exception {
        CryptoPriceDTO item = super.doRead();
        String filename = resource.getFilename();
        ZonedDateTime lastModified = getLastModifiedDate();

        if (item != null) {
            CryptoFileImport cryptoFileImport = cryptoFileImportRepository.findByCryptoSymbolAndFileNameAndLastModifiedDate
                    (item.getSymbol(), filename, lastModified);

            if (cryptoFileImport == null) {
                return new CryptoPriceDTO(item.getSymbol(), item.getPriceTimestamp(), item.getPrice());
            } else {
                return null;
            }
        } else { //end of the file.
            String[] parts = filename.split("_");
            //Save Crypto file details
            CryptoFileImport cryptoFileImport = cryptoFileImportRepository.findByCryptoSymbolAndFileNameAndLastModifiedDate
                    (parts[0], filename, lastModified);
            Crypto crypto = cryptoRepository.findBySymbol(parts[0]);

            if (cryptoFileImport == null) {
                ///Update existing crypto file details
                CryptoFileImport cryptoFileImportBuilder = CryptoFileImport
                        .builder()
                        .fileName(filename)
                        .lastModifiedDate(lastModified)
                        .crypto(Crypto.builder().symbol(crypto.getSymbol()).id(crypto.getId()).build())
                        .build();
                cryptoFileImportRepository.save(cryptoFileImportBuilder);
            }
        }
        return null;
    }

    private LocalDateTime getTimeStamp(Long timeStamp) throws IOException {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        return instant.atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    private ZonedDateTime getLastModifiedDate() throws IOException {
        long millis = Files.getLastModifiedTime(Paths.get(resource.getURI())).toMillis();
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    private LineMapper<CryptoPriceDTO> createCryptoPriceLineMapper() {
        DefaultLineMapper<CryptoPriceDTO> cryptoPriceMapper = new DefaultLineMapper<>();

        LineTokenizer tokenizer = createCryptoPriceLineTokenizer();
        cryptoPriceMapper.setLineTokenizer(tokenizer);

        FieldSetMapper<CryptoPriceDTO> priceInformationMapper = createCryptoPriceInformationMapper();
        cryptoPriceMapper.setFieldSetMapper(priceInformationMapper);

        return cryptoPriceMapper;
    }

    private LineTokenizer createCryptoPriceLineTokenizer() {
        DelimitedLineTokenizer cryptoPriceLineTokenizer = new DelimitedLineTokenizer();
        cryptoPriceLineTokenizer.setDelimiter(",");
        cryptoPriceLineTokenizer.setNames(new String[]{"timestamp", "symbol", "price"});
        return cryptoPriceLineTokenizer;
    }

    private FieldSetMapper<CryptoPriceDTO> createCryptoPriceInformationMapper() {
        return fieldSet -> {
            try {
                return new CryptoPriceDTO(
                        fieldSet.readString("symbol"),
                        getTimeStamp(fieldSet.readLong("timestamp")),
                        fieldSet.readBigDecimal("price")
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
