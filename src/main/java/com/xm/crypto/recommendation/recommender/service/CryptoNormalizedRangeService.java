package com.xm.crypto.recommendation.recommender.service;

import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.recommender.persistence.repository.CryptoNormalizedRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        return List.of();
    }

    public Optional<CryptoRecommenderResponseDTO> findCryptoWithHighestNormalizedRange(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        List<Object[]> result = cryptoNormalizedRangeRepository.
                findCryptoWithHighestNormalizedRangeForSpecificDay(startDateTime, endDateTime);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        Object[] row = result.get(0);
        return Optional.ofNullable(CryptoRecommenderResponseDTO.builder()
                .highestNormalizedRange((BigDecimal) row[1])
                .symbol((String) row[0])
                .build());
    }
}
