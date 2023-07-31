package com.xm.crypto.recommendation.csvimporter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoStatsDTO {

    private Long cryptoId;
    private LocalDate startDate;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal oldestPrice;
    private BigDecimal newestPrice;

}
