package com.xm.crypto.recommendation.recommender.service;

import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.persistence.repository.CryptoNormalizedRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoNormalizedRangeService {

    private CryptoNormalizedRangeRepository cryptoNormalizedRangeRepository;

    @Autowired
    public CryptoNormalizedRangeService(CryptoNormalizedRangeRepository cryptoNormalizedRangeRepository) {
        this.cryptoNormalizedRangeRepository = cryptoNormalizedRangeRepository;
    }

    public List<CryptoNormalizedRangeDTO> findCryptoWithNormalizedRange() {
        List<Object[]> results = cryptoNormalizedRangeRepository.findCryptoWithNormalizedRange();
        if (results != null) {
            return results.stream()
                    .map(result -> new CryptoNormalizedRangeDTO(
                            (String) result[0],
                            (BigDecimal) result[1]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
