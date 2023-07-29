package com.xm.crypto.recommendation.persistence.repositories;

import com.xm.crypto.recommendation.persistence.entities.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {
    Crypto findBySymbol(String symbol);
}
