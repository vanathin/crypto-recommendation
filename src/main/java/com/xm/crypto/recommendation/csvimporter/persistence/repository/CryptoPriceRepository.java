package com.xm.crypto.recommendation.csvimporter.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {
    @Query("SELECT CASE WHEN COUNT(cp) > 0 THEN TRUE ELSE FALSE END FROM CryptoPrice cp WHERE " +
            "cp.priceTimestamp = :priceTimestamp AND cp.crypto.symbol = :cryptoSymbol")
    boolean existsByPriceTimestampAndCryptoSymbol(@Param("priceTimestamp") ZonedDateTime priceTimestamp,
                                                  @Param("cryptoSymbol") String cryptoSymbol);

    @Modifying
    @Query("DELETE FROM CryptoPrice cp WHERE cp.crypto.symbol = :symbol")
    void deleteBySymbol(@Param("symbol") String symbol);


    //CryptoStatsProjection getCryptoStats(@Param("symbol") String symbol, @Param("timeFrame") Integer timeFrame);
}
