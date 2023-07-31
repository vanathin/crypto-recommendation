package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CryptoStatsAggregateService {

    private final CryptoStatsAggregateRepository cryptoStatsRepository;

    @Autowired
    public CryptoStatsAggregateService(CryptoStatsAggregateRepository cryptoStatsRepository) {
        this.cryptoStatsRepository = cryptoStatsRepository;
    }

    public CryptoStatsDTO getCryptoStatsAggregationForGivenSymbol(String symbol) {
        List<Object[]> results = cryptoStatsRepository.findAggregatedCryptoStats(symbol);
        if(results != null){
            for (Object[] result : results) {
                return CryptoStatsDTO.builder()
                        .symbol((String) result[0])
                        .minValue((BigDecimal) result[1])
                        .maxValue((BigDecimal) result[2])
                        .oldestValue((BigDecimal) result[3])
                        .newestValue((BigDecimal) result[4])
                        .build();
            }
        }
        return null;
    }
}
