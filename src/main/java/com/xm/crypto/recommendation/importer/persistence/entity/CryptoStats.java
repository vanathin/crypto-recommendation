package com.xm.crypto.recommendation.importer.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Table(name = "crypto_stats")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name="crypto_id")
    private Crypto crypto;

    @Column(name = "start_date_of_month")
    private LocalDate startDateOfMonth;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "oldest_price")
    private BigDecimal oldestPrice;

    @Column(name = "newest_price")
    private BigDecimal newestPrice;

}

