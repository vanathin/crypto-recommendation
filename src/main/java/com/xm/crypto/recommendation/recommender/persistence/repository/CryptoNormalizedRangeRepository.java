package com.xm.crypto.recommendation.recommender.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query(value = "SELECT c.symbol, ((max_price - min_price) / min_price) AS normalized_range " +
            "FROM (SELECT crypto_id, MIN(price) AS min_price, MAX(price) AS max_price " +
            "FROM crypto_price " +
            "WHERE price_timestamp >= :startDateTime AND price_timestamp < :endDateTime " +
            "GROUP BY crypto_id) cp " +
            "JOIN crypto c ON c.id = cp.crypto_id " +
            "ORDER BY normalized_range DESC " +
            "LIMIT 1",
            nativeQuery = true)
    List<Object[]> findCryptoWithHighestNormalizedRangeForSpecificDay(@Param("startDateTime") LocalDateTime startDateTime,
                                                                      @Param("endDateTime") LocalDateTime endDateTime);

}
