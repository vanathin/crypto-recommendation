package com.xm.crypto.recommendation.importer.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CryptoPriceDTO {

    private String symbol;

    private LocalDateTime priceTimestamp;

    private BigDecimal price;
}
