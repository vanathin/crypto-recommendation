package com.xm.crypto.recommendation.info.service;

import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoRepository;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.persistence.entity.CryptoStatsProjection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CryptoInfoService {

    private final CryptoRepository cryptoRepository;

    public CryptoInfoService(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    public CryptoStatsDTO getCryptoInfo(String symbol) {
        CryptoStatsProjection cryptoStatsProjection =null;
        //cryptoRepository.findCryptoStatsBySymbol(symbol);

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
