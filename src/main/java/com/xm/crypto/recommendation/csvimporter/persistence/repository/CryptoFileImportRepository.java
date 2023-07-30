package com.xm.crypto.recommendation.csvimporter.persistence.repository;

import com.xm.crypto.recommendation.csvimporter.persistence.entity.CryptoFileImport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

public interface CryptoFileImportRepository extends JpaRepository<CryptoFileImport, Long> {

    CryptoFileImport findByCryptoIdAndFileNameAndLastModifiedDate(Long id, String filename, ZonedDateTime lastModified);
}
