package com.xm.crypto.recommendation.recommender.service;

import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.persistence.repository.CryptoNormalizedRangeRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CryptoNormalizedRangeServiceTest {

    @Mock
    private CryptoNormalizedRangeRepository cryptoNormalizedRangeRepository;

    @InjectMocks
    private CryptoNormalizedRangeService cryptoNormalizedRangeService;

    @BeforeEach
    public void setup() {
        // Setup mock data for the repository
        List<Object[]> mockResults = Arrays.asList(
                new Object[] { "BTC", BigDecimal.valueOf(100) },
                new Object[] { "ETH", BigDecimal.valueOf(50) },
                new Object[] { "XRP", BigDecimal.valueOf(200) }
        );

        // Mock the repository method call
        when(cryptoNormalizedRangeRepository.findCryptoWithNormalizedRange()).thenReturn(mockResults);
    }

    @Test
    public void testFindCryptoWithNormalizedRange() {
        // When
        List<CryptoNormalizedRangeDTO> result = cryptoNormalizedRangeService.findCryptoWithNormalizedRange();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());

        // Assert individual DTO elements (assuming the order is preserved)
        assertEquals("BTC", result.get(0).getSymbol());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getNormalizedRange());

        assertEquals("ETH", result.get(1).getSymbol());
        assertEquals(BigDecimal.valueOf(50), result.get(1).getNormalizedRange());

        assertEquals("XRP", result.get(2).getSymbol());
        assertEquals(BigDecimal.valueOf(200), result.get(2).getNormalizedRange());
    }

}