package com.xm.crypto.recommendation.csv.writer;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoFileImportDTO;
import com.xm.crypto.recommendation.csvimporter.dto.CryptoPriceDTO;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import com.xm.crypto.recommendation.csvimporter.batch.writer.CryptoPriceWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {"spring.batch.job.enabled=false"})
public class CryptoPriceWriterTest {
    @InjectMocks
    private CryptoPriceWriter cryptoPriceWriter;

    @Mock
    private CryptoRepository cryptoRepository;

    @Mock
    private CryptoFileImportRepository cryptoFileImportRepository;

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    private CryptoPriceDTO priceDTO;
    private Crypto crypto;
    private CryptoFileImport cryptoFileImport;

    @BeforeEach
    public void setUp() {
        // Create your test data
        priceDTO = CryptoPriceDTO.builder()
                .symbol("BTC")
                .price(BigDecimal.valueOf(45000.00))
                .priceTimestamp(LocalDateTime.from(ZonedDateTime.ofInstant(Instant.ofEpochMilli(Instant.now().toEpochMilli()), ZoneId.systemDefault())))
                .build();

        crypto = new Crypto();
        crypto.setId(1L);
        crypto.setSymbol("BTC");

        cryptoFileImport = new CryptoFileImport();
        cryptoFileImport.setFileName("btc.csv");
    }

    @Test
    public void testWrite() throws Exception {
        when(cryptoRepository.findBySymbol(anyString())).thenReturn(null);
        when(cryptoRepository.save(any(Crypto.class))).thenReturn(crypto);
        when(cryptoFileImportRepository.findByCryptoSymbolAndFileNameAndLastModifiedDate(anyString(), anyString(), any(ZonedDateTime.class))).thenReturn(null);
        when(cryptoFileImportRepository.save(any(CryptoFileImport.class))).thenReturn(cryptoFileImport);

        cryptoPriceWriter.write(Collections.singletonList(priceDTO));

        verify(cryptoRepository, times(1)).findBySymbol(anyString());
        verify(cryptoRepository, times(1)).save(any(Crypto.class));
        verify(cryptoFileImportRepository, times(1)).findByCryptoSymbolAndFileNameAndLastModifiedDate(anyString(), anyString(), any(ZonedDateTime.class));
        verify(cryptoFileImportRepository, times(1)).save(any(CryptoFileImport.class));
        verify(cryptoPriceRepository, times(1)).saveAll(anyList());
    }

}