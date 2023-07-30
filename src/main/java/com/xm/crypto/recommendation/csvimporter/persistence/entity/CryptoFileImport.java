package com.xm.crypto.recommendation.csvimporter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private ZonedDateTime lastModifiedDate;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private ZonedDateTime updatedAt;
}
