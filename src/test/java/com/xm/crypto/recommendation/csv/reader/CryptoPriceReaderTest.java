package com.xm.crypto.recommendation.csv.reader;

import com.xm.crypto.recommendation.csv.dto.CryptoFileImportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {"spring.batch.job.enabled=false"})
class CryptoPriceReaderTest {


    private CryptoPriceReader reader;
    private Resource resource;

    @BeforeEach
    public void setup() throws Exception {
        reader = new CryptoPriceReader();
        resource = new ClassPathResource("test.csv");
        reader.setResource(resource);
        reader.afterPropertiesSet(); // You must call this method because it is normally called by Spring to initialize the reader
        reader.open(new ExecutionContext());
    }

    @Test
    public void testDoRead() throws Exception {
        CryptoFileImportDto readItem = reader.read();

        // Assert the readItem
        assertNotNull(readItem);
        assertEquals(1640995200000L, readItem.timestamp());
        assertEquals("XRP", readItem.symbol());
        assertEquals(new BigDecimal("0.8298"), readItem.price());
        assertEquals("test.csv", readItem.filename());
    }
}