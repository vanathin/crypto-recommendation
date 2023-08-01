package com.xm.crypto.recommendation.importer.persistence.repository;

import com.xm.crypto.recommendation.importer.persistence.entity.CryptoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CryptoStatsRepository extends JpaRepository<CryptoStats, Long> {

    @Query(value =
            "SELECT CAST(derived.crypto_id AS UNSIGNED) AS crypto_id, " +
                    "MIN(derived.price) as min_price," +
                    "MAX(derived.price) as max_price, " +
                    ":startDate, "+
                    "derived.oldest_price, " +
                    "derived.newest_price " +
                    "FROM ( " +
                    "  SELECT cp.crypto_id, cp.price, " +
                    "  FIRST_VALUE(cp.price) OVER (PARTITION BY cp.crypto_id ORDER BY cp.price_timestamp ASC) as oldest_price, " +
                    "  FIRST_VALUE(cp.price) OVER (PARTITION BY cp.crypto_id ORDER BY cp.price_timestamp DESC) as newest_price " +
                    "  FROM crypto_price cp " +
                    "  WHERE cp.price_timestamp >= :startDateTime AND cp.price_timestamp <= :endDateTime " +
                    ") as derived " +
                    "GROUP BY derived.crypto_id, derived.oldest_price, derived.newest_price",
            nativeQuery = true)
    List<Object[]> findCryptoStats(LocalDate startDate, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
