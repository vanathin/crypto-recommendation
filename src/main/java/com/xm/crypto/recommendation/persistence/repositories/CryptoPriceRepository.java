package com.xm.crypto.recommendation.persistence.repositories;

import com.xm.crypto.recommendation.persistence.entities.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {
}
