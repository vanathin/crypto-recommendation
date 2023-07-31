package com.xm.crypto.recommendation.info.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CryptoStatsAggregateRepository extends JpaRepository<CryptoStats, Long> {

    @Query(value =
            "WITH ranked_dates AS ( " +
                    "    SELECT " +
                    "        cs.crypto_id," +
                    "        cs.min_price," +
                    "        cs.max_price," +
                    "        cs.oldest_price," +
                    "        cs.newest_price," +
                    "        RANK() OVER (PARTITION BY cs.crypto_id ORDER BY cs.start_date_of_month DESC) as rn " +
                    "    FROM " +
                    "        crypto_stats cs " +
                    "    JOIN " +
                    "        crypto c " +
                    "        ON c.id = cs.crypto_id " +
                    "    WHERE " +
                    "        c.symbol = :symbol) " +
                    "SELECT " +
                    "    c.symbol, " +
                    "    MIN(rd.min_price) AS aggregated_min_price, " +
                    "    MAX(rd.max_price) AS aggregated_max_price, " +
                    "    ( " +
                    "        SELECT " +
                    "            rd1.oldest_price " +
                    "        FROM " +
                    "            ranked_dates rd1 " +
                    "        WHERE " +
                    "            rd1.rn = c.time_frame_in_month " +
                    "    ) AS oldest_price, " +
                    "    ( " +
                    "        SELECT " +
                    "            rd2.newest_price " +
                    "        FROM " +
                    "            ranked_dates rd2 " +
                    "        WHERE " +
                    "            rd2.rn = 1 " +
                    "    ) AS newest_price " +
                    "FROM " +
                    "    ranked_dates rd " +
                    "JOIN " +
                    "    crypto c " +
                    "    ON c.id = rd.crypto_id " +
                    "WHERE " +
                    "    rd.rn <= c.time_frame_in_month " +
                    "GROUP BY " +
                    "    c.symbol ",
            nativeQuery = true)
    List<Object[]> findAggregatedCryptoStats(@Param("symbol") String symbol);

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
