package com.xm.crypto.recommendation.recommender.service;

import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.importer.persistence.repository.CryptoRepository;
import com.xm.crypto.recommendation.recommender.dto.projection.CryptoNormalizedRangeProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecommenderService {

    private final CryptoRepository cryptoRepository;

    @Autowired
    public RecommenderService(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    public CryptoRecommenderResponseDTO findByDay(Date date) {
        PageRequest pageRequest = PageRequest.of(0, 1); // Limit to 1
        List<CryptoNormalizedRangeProjection> result = cryptoRepository.findCryptoWithHighestNormalizedRangeForDay(date, pageRequest);

        if (!result.isEmpty()) {
            CryptoNormalizedRangeProjection cryptoProjection = result.get(0);
           return  CryptoRecommenderResponseDTO.builder()
                    .highestNormalizedRange(cryptoProjection.getNormalizedRange())
                    .symbol(cryptoProjection.getSymbol())
                    .build();
        }
        return null;
    }
}


