package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.info.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoStatsAggregateService {

    private final CryptoStatsAggregateRepository cryptoStatsRepository;

    public CryptoStatsAggregateService(CryptoStatsAggregateRepository cryptoStatsRepository) {
        this.cryptoStatsRepository = cryptoStatsRepository;
    }

    public CryptoStatsDTO getCryptoStatsAggregationForGivenSymbol(String symbol) {
        List<Object[]> results = cryptoStatsRepository.findAggregatedCryptoStats(symbol);
        for (Object[] result : results) {
            return CryptoStatsDTO.builder()
                    .symbol((String) result[0])
                    .minValue((BigDecimal) result[1])
                    .maxValue((BigDecimal) result[2])
                    .oldestValue((BigDecimal) result[3])
                    .newestValue((BigDecimal) result[4])
                    .build();
        }
        return null;
    }

    public List<CryptoNormalizedRangeDTO> findCryptoWithNormalizedRange() {
        List<Object[]> results = cryptoStatsRepository.findCryptoWithNormalizedRange();
        return results.stream()
                .map(result -> new CryptoNormalizedRangeDTO(
                        (String) result[0],
                        (BigDecimal) result[1]))
                .collect(Collectors.toList());
    }
}
