package com.xm.crypto.recommendation.importer.persistence.repository;

import com.xm.crypto.recommendation.importer.persistence.entity.CryptoFileImport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface CryptoFileImportRepository extends JpaRepository<CryptoFileImport, Long> {

    @Query("SELECT cf FROM CryptoFileImport cf " +
            "JOIN cf.crypto c " +
            "WHERE c.symbol = :symbol " +
            "AND cf.fileName = :fileName " +
            "AND cf.lastModifiedDate = :lastModifiedDate")
    CryptoFileImport findByCryptoSymbolAndFileNameAndLastModifiedDate(String symbol, String fileName, ZonedDateTime lastModifiedDate);

    @Query("SELECT cfi FROM CryptoFileImport cfi JOIN cfi.crypto c WHERE c.symbol = :symbol")
    CryptoFileImport findByCryptoSymbol(@Param("symbol") String symbol);

}
