package com.xm.crypto.recommendation.info.persistence.entity;

import java.math.BigDecimal;

public interface CryptoStatsProjection {

    String getSymbol();
    BigDecimal getOldestValue();

    BigDecimal getNewestValue();

    BigDecimal getMinValue();

    BigDecimal getMaxValue();
}
