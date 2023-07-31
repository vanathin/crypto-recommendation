package com.xm.crypto.recommendation.csvimporter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record CryptoFileImportDTO(Long timestamp, String symbol, BigDecimal price, String filename, ZonedDateTime lastModified) {
}
