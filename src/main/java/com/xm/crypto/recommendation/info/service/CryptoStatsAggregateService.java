package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Service
public class CryptoStatsAggregateService {

    private final CryptoStatsAggregateRepository cryptoStatsRepository;

    public CryptoStatsAggregateService(CryptoStatsAggregateRepository cryptoStatsRepository) {
        this.cryptoStatsRepository = cryptoStatsRepository;
    }

    public CryptoStatsDTO getCryptoStatsForGivenSymbol(String symbol) {
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
}
