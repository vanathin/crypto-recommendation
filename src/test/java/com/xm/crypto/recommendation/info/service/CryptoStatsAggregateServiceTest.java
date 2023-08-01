package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import com.xm.crypto.recommendation.importer.persistence.entity.Crypto;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CryptoStatsAggregateServiceTest {

    @Mock
    private CryptoStatsAggregateRepository cryptoStatsRepository;

    @Mock
    private CryptoRepository cryptoRepository;

    @InjectMocks
    private CryptoStatsAggregateService cryptoStatsAggregateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCryptoStatsAggregationForGivenSymbol_Success() {
        // Given
        String symbol = "BTC";
        Crypto crypto = new Crypto();
        crypto.setSymbol(symbol);

        // Mock the behavior of the cryptoRepository.findBySymbol method
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(crypto);

        // Mock the behavior of the cryptoStatsRepository.findAggregatedCryptoStats method
        List<Object[]> results = new ArrayList<>();
        Object[] result = new Object[]{"BTC", new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("50"), new BigDecimal("150")};
        results.add(result);
        when(cryptoStatsRepository.findAggregatedCryptoStats(symbol)).thenReturn(results);

        // When
        CryptoStatsDTO cryptoStatsDTO = cryptoStatsAggregateService.getCryptoStatsAggregationForGivenSymbol(symbol);

        // Then
        assertNotNull(cryptoStatsDTO);
        assertEquals("BTC", cryptoStatsDTO.getSymbol());
        assertEquals(new BigDecimal("100"), cryptoStatsDTO.getMinValue());
        assertEquals(new BigDecimal("200"), cryptoStatsDTO.getMaxValue());
        assertEquals(new BigDecimal("50"), cryptoStatsDTO.getOldestValue());
        assertEquals(new BigDecimal("150"), cryptoStatsDTO.getNewestValue());

        // Verify that the methods in the cryptoRepository and cryptoStatsRepository were called
        verify(cryptoRepository).findBySymbol(symbol);
        verify(cryptoStatsRepository).findAggregatedCryptoStats(symbol);
    }

    @Test
    public void testGetCryptoStatsAggregationForGivenSymbol_CryptoNotFound() {
        // Given
        String symbol = "ETH";

        // Mock the behavior of the cryptoRepository.findBySymbol method to return null
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(null);

        // When and Then
        assertThrows(CryptoNotFoundDomainException.class,
                () -> cryptoStatsAggregateService.getCryptoStatsAggregationForGivenSymbol(symbol));

        // Verify that the cryptoRepository.findBySymbol method was called
        verify(cryptoRepository).findBySymbol(symbol);
        // Verify that the cryptoStatsRepository.findAggregatedCryptoStats method was not called
        verifyNoInteractions(cryptoStatsRepository);
    }

    @Test
    public void testGetCryptoStatsAggregationForGivenSymbol_NoResults() {
        // Given
        String symbol = "LTC";
        Crypto crypto = new Crypto();
        crypto.setSymbol(symbol);

        // Mock the behavior of the cryptoRepository.findBySymbol method
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(crypto);

        // Mock the behavior of the cryptoStatsRepository.findAggregatedCryptoStats method to return null
        when(cryptoStatsRepository.findAggregatedCryptoStats(symbol)).thenReturn(null);

        // When
        CryptoStatsDTO cryptoStatsDTO = cryptoStatsAggregateService.getCryptoStatsAggregationForGivenSymbol(symbol);

        // Then
        assertNull(cryptoStatsDTO);

        // Verify that the methods in the cryptoRepository and cryptoStatsRepository were called
        verify(cryptoRepository).findBySymbol(symbol);
        verify(cryptoStatsRepository).findAggregatedCryptoStats(symbol);
    }
}
