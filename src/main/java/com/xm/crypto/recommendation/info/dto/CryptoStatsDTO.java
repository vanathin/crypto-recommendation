package com.xm.crypto.recommendation.info.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class CryptoStatsDTO {
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal oldestValue;
    private BigDecimal newestValue;
}
