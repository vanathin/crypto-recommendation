package com.xm.crypto.recommendation.recommender.dto.projection;

import java.math.BigDecimal;

public interface CryptoNormalizedRangeProjection {
    Long getCryptoId();
    String getSymbol();
    BigDecimal getNormalizedRange();
}
