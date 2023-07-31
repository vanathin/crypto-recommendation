package com.xm.crypto.recommendation.recommender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CryptoNormalizedRangeDTO {
    private String symbol;
    private BigDecimal normalizedRange;
}
