package com.xm.crypto.recommendation.recommender.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CryptoNormalizedRangeRepository extends JpaRepository<CryptoStats, Long> {
    @Query(value = "SELECT c.symbol, ((MAX(cs.max_price) - MIN(cs.min_price)) / MIN(cs.min_price)) AS normalized_range " +
            "FROM crypto_stats cs " +
            "JOIN crypto c ON c.id = cs.crypto_id " +
            "WHERE cs.start_date_of_month >= (" +
            "    SELECT MAX(start_date_of_month) FROM crypto_stats WHERE crypto_id = cs.crypto_id" +
            ") - INTERVAL (c.time_frame_in_month - 1) MONTH " +
            "GROUP BY c.symbol " +
            "ORDER BY normalized_range DESC", nativeQuery = true)
    List<Object[]> findCryptoWithNormalizedRange();
}
