package com.xm.crypto.recommendation.importer.batch.writer;


import com.xm.crypto.recommendation.importer.dto.CryptoPriceDTO;
import com.xm.crypto.recommendation.importer.persistence.entity.Crypto;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoFileImportRepository;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CryptoPriceWriterTest {

    @InjectMocks
    private CryptoPriceWriter cryptoPriceWriter;

    @Mock
    private CryptoRepository cryptoRepository;

    @Mock
    private CryptoFileImportRepository cryptoFileImportRepository;

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void write() throws Exception {
        // Arrange
        LocalDateTime testDateTime = LocalDateTime.of(2023, 8, 1, 12, 0);
        CryptoPriceDTO cryptoPriceDTO = new CryptoPriceDTO("BTC", testDateTime, BigDecimal.valueOf(40000.0));
        Crypto testCrypto = Crypto.builder()
                .symbol("BTC")
                .supported(true)
                .timeFrameInMonth(1)
                .id(1L)
                .build();
        when(cryptoRepository.findBySymbol("BTC")).thenReturn(null);
        when(cryptoRepository.save(any())).thenReturn(testCrypto);

        // Act
        cryptoPriceWriter.write(Collections.singletonList(cryptoPriceDTO));

        // Assert
        verify(cryptoRepository, times(1)).findBySymbol("BTC");
        verify(cryptoRepository, times(1)).save(any());
        verify(cryptoPriceRepository, times(1)).saveAll(any());
    }

    @Test
    void write_withExistingCrypto_doesNotCreateNewCrypto() throws Exception {
        // Arrange
        LocalDateTime testDateTime = LocalDateTime.of(2023, 8, 1, 12, 0);
        CryptoPriceDTO cryptoPriceDTO = new CryptoPriceDTO("BTC", testDateTime, BigDecimal.valueOf(40000.0));
        Crypto existingCrypto = Crypto.builder().id(1L).symbol("BTC").supported(true).timeFrameInMonth(1).build();
        when(cryptoRepository.findBySymbol("BTC")).thenReturn(existingCrypto);

        // Act
        cryptoPriceWriter.write(Collections.singletonList(cryptoPriceDTO));

        // Assert
        verify(cryptoRepository, times(1)).findBySymbol("BTC");
        verify(cryptoRepository, times(0)).save(any()); // assert that new Crypto is not saved
        verify(cryptoPriceRepository, times(1)).saveAll(any());
    }


    @Test
    void write_withMultipleCryptos_updatesDateTimeForProcessingMonthCorrectly() throws Exception {
        // Arrange
        LocalDateTime earlierDateTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        CryptoPriceDTO earlierCryptoPriceDTO = new CryptoPriceDTO("BTC", earlierDateTime, BigDecimal.valueOf(40000.0));
        LocalDateTime laterDateTime = LocalDateTime.of(2023, 12, 31, 12, 0);
        CryptoPriceDTO laterCryptoPriceDTO = new CryptoPriceDTO("BTC", laterDateTime, BigDecimal.valueOf(45000.0));
        Crypto existingCrypto = Crypto.builder().id(1L).symbol("BTC").supported(true).timeFrameInMonth(1).build();
        when(cryptoRepository.findBySymbol("BTC")).thenReturn(existingCrypto);
        // Act
        cryptoPriceWriter.write(Arrays.asList(earlierCryptoPriceDTO, laterCryptoPriceDTO));

        // Assert
        assertEquals(earlierDateTime, cryptoPriceWriter.getDateTimeForProcessingMonth());
    }


}
