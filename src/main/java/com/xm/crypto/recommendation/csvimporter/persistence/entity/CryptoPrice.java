package com.xm.crypto.recommendation.csvimporter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(name="id")
    private Long id;

    @OneToOne
    @JoinColumn(name="crypto_id")
    private Crypto crypto;

    @Column(name = "price_timestamp", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime priceTimestamp;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
}
