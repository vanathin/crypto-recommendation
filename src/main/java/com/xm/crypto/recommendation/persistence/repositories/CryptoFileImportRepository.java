package com.xm.crypto.recommendation.persistence.repositories;

import com.xm.crypto.recommendation.persistence.entities.CryptoFileImport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

public interface CryptoFileImportRepository extends JpaRepository<CryptoFileImport, Long> {

    CryptoFileImport findByCryptoIdAndFileNameAndLastModifiedDate(Long id, String filename, ZonedDateTime lastModified);
}
