package com.xm.crypto.recommendation.csv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record CryptoFileImportDto(Long timestamp, String symbol, BigDecimal price, String filename, ZonedDateTime lastModified) {
}
