package com.xm.crypto.recommendation.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.Id;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString
@Table(name="crypto")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol", nullable = false, unique = true, length = 255)
    private String symbol;

    @Column(name = "supported", nullable = false, columnDefinition = "boolean default false")
    private boolean supported;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private ZonedDateTime updatedAt;

}
