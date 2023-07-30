package com.xm.crypto.recommendation.csvimporter.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.Crypto;

import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.recommender.dto.projection.CryptoNormalizedRangeResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {
    Crypto findBySymbol(String symbol);

    @Query("SELECT cp.crypto.id as cryptoId, cp.crypto.symbol as symbol, (MAX(cp.price) - MIN(cp.price)) / MIN(cp.price) as normalizedRange " +
            "FROM CryptoPrice cp " +
            "WHERE DATE(cp.priceTimestamp) = DATE(:date) " +
            "GROUP BY cp.crypto " +
            "ORDER BY normalizedRange DESC")
    List<CryptoNormalizedRangeResult> findCryptoWithHighestNormalizedRangeForDay(@Param("date") Date date, Pageable pageable);
}
