package com.xm.crypto.recommendation.persistence.entities;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Getter
@Setter
@Builder
@ToString
@Table(name="crypto_price")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="crypto_id")
    private Crypto crypto;

    @Column(name = "price_timestamp", nullable = false)
    private ZonedDateTime priceTimestamp;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
