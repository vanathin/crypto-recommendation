package com.xm.crypto.recommendation.csvimporter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString
@Table(name="crypto_file_import")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CryptoFileImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="crypto_id")
    private Crypto crypto;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "time_frame", nullable = false)
    private Integer timeFrame;

    @Column(name = "last_modified_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime lastModifiedDate;
}
