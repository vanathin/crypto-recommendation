package com.xm.crypto.recommendation.recommender.service;

import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.recommender.persistence.repository.CryptoNormalizedRangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CryptoNormalizedRangeServiceTest {


    @Mock
    private CryptoNormalizedRangeRepository cryptoNormalizedRangeRepository;

    @InjectMocks
    private CryptoNormalizedRangeService cryptoNormalizedRangeService;

    @Test
    public void testFindCryptoWithNormalizedRange() {
        // Given
        Object[] row1 = {"BTC", BigDecimal.valueOf(0.25)};
        Object[] row2 = {"ETH", BigDecimal.valueOf(0.35)};
        List<Object[]> results = Arrays.asList(row1, row2);
        when(cryptoNormalizedRangeRepository.findCryptoWithNormalizedRange()).thenReturn(results);

        // When
        List<CryptoNormalizedRangeDTO> dtoList = cryptoNormalizedRangeService.findCryptoWithNormalizedRange();

        // Then
        assertEquals(2, dtoList.size());
        assertEquals("BTC", dtoList.get(0).getSymbol());
        assertEquals(BigDecimal.valueOf(0.25), dtoList.get(0).getNormalizedRange());
        assertEquals("ETH", dtoList.get(1).getSymbol());
        assertEquals(BigDecimal.valueOf(0.35), dtoList.get(1).getNormalizedRange());
    }

    @Test
    public void testFindCryptoWithNormalizedRange_NoResults() {
        // Given
        when(cryptoNormalizedRangeRepository.findCryptoWithNormalizedRange()).thenReturn(null);

        // When
        List<CryptoNormalizedRangeDTO> dtoList = cryptoNormalizedRangeService.findCryptoWithNormalizedRange();

        // Then
        assertTrue(dtoList.isEmpty());
    }

    @Test
    public void testFindCryptoWithNormalizedRange_WithResults() {
        // Given
        Object[] result1 = { "BTC", new BigDecimal("0.25") };
        Object[] result2 = { "ETH", new BigDecimal("0.15") };
        List<Object[]> results = Arrays.asList(result1, result2);

        when(cryptoNormalizedRangeRepository.findCryptoWithNormalizedRange()).thenReturn(results);

        // When
        List<CryptoNormalizedRangeDTO> dtoList = cryptoNormalizedRangeService.findCryptoWithNormalizedRange();

        // Then
        assertFalse(dtoList.isEmpty());
        assertEquals(2, dtoList.size());

        CryptoNormalizedRangeDTO dto1 = dtoList.get(0);
        assertEquals("BTC", dto1.getSymbol());
        assertEquals(new BigDecimal("0.25"), dto1.getNormalizedRange());

        CryptoNormalizedRangeDTO dto2 = dtoList.get(1);
        assertEquals("ETH", dto2.getSymbol());
        assertEquals(new BigDecimal("0.15"), dto2.getNormalizedRange());
    }


    @Test
    public void testFindCryptoWithHighestNormalizedRange() {
        // Given
        LocalDate date = LocalDate.of(2023, 7, 31);
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        Object[] row = {"BTC", BigDecimal.valueOf(0.25)};
        List<Object[]> listOfArrays = new ArrayList<>();
        listOfArrays.add(row);

        when(cryptoNormalizedRangeRepository.findCryptoWithHighestNormalizedRangeForSpecificDay(startDateTime, endDateTime))
                .thenReturn(listOfArrays);

        // When
        Optional<CryptoRecommenderResponseDTO> response = cryptoNormalizedRangeService.findCryptoWithHighestNormalizedRange(date);

        // Then
        assertTrue(response.isPresent());
        assertEquals("BTC", response.get().getSymbol());
        assertEquals(BigDecimal.valueOf(0.25), response.get().getHighestNormalizedRange());
    }

    @Test
    public void testFindCryptoWithHighestNormalizedRange_NoResult() {
        // Given
        LocalDate date = LocalDate.of(2023, 7, 31);
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        List<Object[]> result = new ArrayList<>();

        when(cryptoNormalizedRangeRepository.findCryptoWithHighestNormalizedRangeForSpecificDay(startDateTime, endDateTime))
                .thenReturn(result);

        // When
        Optional<CryptoRecommenderResponseDTO> response = cryptoNormalizedRangeService.findCryptoWithHighestNormalizedRange(date);

        // Then
        assertFalse(response.isPresent());
    }

}