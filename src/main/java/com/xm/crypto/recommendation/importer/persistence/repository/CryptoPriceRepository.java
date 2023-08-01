package com.xm.crypto.recommendation.importer.persistence.repository;

import com.xm.crypto.recommendation.importer.persistence.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {
}
