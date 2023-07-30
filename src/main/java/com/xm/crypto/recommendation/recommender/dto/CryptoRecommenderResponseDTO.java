package com.xm.crypto.recommendation.recommender.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class CryptoRecommenderResponseDTO {

    private String symbol;

    private BigDecimal highestNormalizedRange;
}
