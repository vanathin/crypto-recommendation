package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.repository.CryptoStatsAggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CryptoStatsAggregateService {

    private final CryptoStatsAggregateRepository cryptoStatsRepository;

    private final CryptoRepository cryptoRepository;

    @Autowired
    public CryptoStatsAggregateService(CryptoStatsAggregateRepository cryptoStatsRepository, CryptoRepository cryptoRepository) {
        this.cryptoStatsRepository = cryptoStatsRepository;
        this.cryptoRepository = cryptoRepository;
    }

    public CryptoStatsDTO getCryptoStatsAggregationForGivenSymbol(String symbol) {
        Optional.ofNullable(cryptoRepository.findBySymbol(symbol))
                .orElseThrow(() -> new CryptoNotFoundDomainException("Given symbol not found"));

        List<Object[]> results = cryptoStatsRepository.findAggregatedCryptoStats(symbol);
        if(results != null) {
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
        return  null;
    }
}
