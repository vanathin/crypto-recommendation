package com.xm.crypto.recommendation.csvimporter.batch.writer;

import com.xm.crypto.recommendation.csvimporter.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;
import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoStats;
import com.xm.crypto.recommendation.csvimporter.persistence.repository.CryptoStatsRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class CryptoStatsWriter implements ItemWriter<CryptoStatsDTO> {

    private final CryptoStatsRepository cryptoStatsRepository;

    @Autowired
    public CryptoStatsWriter(CryptoStatsRepository cryptoStatsRepository) {
        this.cryptoStatsRepository = cryptoStatsRepository;
    }

    @Override
    @Transactional
    public void write(List<? extends CryptoStatsDTO> statsResultList) throws Exception {
        List<CryptoStats> cryptoStatsList = new ArrayList<>();

        for (CryptoStatsDTO statsResult : statsResultList) {
            CryptoStats cryptoStats = new CryptoStats();
            cryptoStats.setCrypto(Crypto.builder().id(statsResult.getCryptoId()).build());
            cryptoStats.setStartDateOfMonth(statsResult.getStartDate());
            cryptoStats.setMinPrice(statsResult.getMinPrice());
            cryptoStats.setMaxPrice(statsResult.getMaxPrice());
            cryptoStats.setOldestPrice(statsResult.getOldestPrice());
            cryptoStats.setNewestPrice(statsResult.getNewestPrice());
            cryptoStatsList.add(cryptoStats);
        }

        cryptoStatsRepository.saveAll(cryptoStatsList);
    }

}
