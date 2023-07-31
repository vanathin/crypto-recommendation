package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoPriceRepository;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.entity.CryptoStatsProjection;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CryptoInfoService {

    private final CryptoPriceRepository cryptoPriceRepository;

    public CryptoInfoService(CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    public CryptoStatsDTO getCryptoInfo(String symbol) {
        CryptoStatsProjection cryptoStatsProjection = null;
                //cryptoPriceRepository.getCryptoStats(symbol);

        if(cryptoStatsProjection != null && !StringUtils.isEmpty(cryptoStatsProjection.getSymbol())){
            return CryptoStatsDTO.builder()
                    .maxValue(cryptoStatsProjection.getMaxValue())
                    .minValue(cryptoStatsProjection.getMinValue())
                    .newestValue(cryptoStatsProjection.getNewestValue())
                    .oldestValue(cryptoStatsProjection.getOldestValue())
                    .build();
        }
        return null;
    }
}
