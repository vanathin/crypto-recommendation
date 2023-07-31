package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CryptoStatsAggregateServiceTest {
    @Mock
    private CryptoStatsAggregateRepository cryptoStatsRepository;

    @InjectMocks
    private CryptoStatsAggregateService cryptoStatsService;

    @BeforeEach
    public void setup() {
        // Setup mock data for the repository
        List<Object[]> mockResults = Arrays.asList(
                new Object[] { "BTC", BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(50), BigDecimal.valueOf(180) },
                new Object[] { "ETH", BigDecimal.valueOf(50), BigDecimal.valueOf(100), BigDecimal.valueOf(30), BigDecimal.valueOf(90) }
        );

        // Mock the repository method call
        when(cryptoStatsRepository.findAggregatedCryptoStats(anyString())).thenReturn(mockResults);
    }

    @Test
    public void testGetCryptoStatsAggregationForGivenSymbol() {
        // Given
        String symbol = "BTC";

        // When
        CryptoStatsDTO result = cryptoStatsService.getCryptoStatsAggregationForGivenSymbol(symbol);

        // Then
        assertNotNull(result);
        assertEquals("BTC", result.getSymbol());
        assertEquals(BigDecimal.valueOf(100), result.getMinValue());
        assertEquals(BigDecimal.valueOf(200), result.getMaxValue());
        assertEquals(BigDecimal.valueOf(50), result.getOldestValue());
        assertEquals(BigDecimal.valueOf(180), result.getNewestValue());
    }

    @Test
    public void testGetCryptoStatsAggregationForNonExistingSymbol() {
        // Given
        String symbol = "XYZ";

        // Mock the repository method call
        when(cryptoStatsRepository.findAggregatedCryptoStats(anyString())).thenReturn(null);

        // When
        CryptoStatsDTO result = cryptoStatsService.getCryptoStatsAggregationForGivenSymbol(symbol);

        // Then
        assertNull(result);
    }


}