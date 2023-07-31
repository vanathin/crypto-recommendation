package com.xm.crypto.recommendation.csvimporter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CryptoStatsProjection {
    Long getCryptoId();
    LocalDate getStartDate();
    BigDecimal getMinPrice();
    BigDecimal getMaxPrice();
    BigDecimal getOldestPrice();
    BigDecimal getNewestPrice();
}
