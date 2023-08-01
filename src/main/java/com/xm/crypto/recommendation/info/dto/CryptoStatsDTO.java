package com.xm.crypto.recommendation.info.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class CryptoStatsDTO {
    private String symbol;
    private BigDecimal minValue;
    private BigDecimal maxValue;

    private BigDecimal oldestValue;
    private BigDecimal newestValue;

    public CryptoStatsDTO(String symbol, BigDecimal minValue, BigDecimal maxValue, BigDecimal oldestValue, BigDecimal newestValue) {
        this.symbol = symbol;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.oldestValue = oldestValue;
        this.newestValue = newestValue;
    }
}
