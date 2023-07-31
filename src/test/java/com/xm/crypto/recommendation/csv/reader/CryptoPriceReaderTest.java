package com.xm.crypto.recommendation.csv.reader;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDTO;
import com.xm.crypto.recommendation.csvimporter.batch.reader.CryptoPriceReader;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {"spring.batch.job.enabled=false"})
class CryptoPriceReaderTest {


    private CryptoPriceReader reader;
    private Resource resource;
    @Mock
    private CryptoFileImportRepository cryptoFileImportRepository;

    @Mock
    private CryptoRepository cryptoRepository;

    @BeforeEach
    public void setup() throws Exception {
        reader = new CryptoPriceReader(cryptoFileImportRepository, cryptoRepository);
        resource = new ClassPathResource("test.csv");
        reader.setResource(resource);
        reader.afterPropertiesSet(); // You must call this method because it is normally called by Spring to initialize the reader
        reader.open(new ExecutionContext());
    }

    @Test
    public void testDoRead() throws Exception {

    }
}