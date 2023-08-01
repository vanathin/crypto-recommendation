    package com.xm.crypto.recommendation.importer.persistence.entity;

    import lombok.*;

    import javax.persistence.Id;

    import javax.persistence.*;

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
        @Column(name = "id")
        private Long id;

        @Column(name = "symbol", nullable = false, unique = true, length = 255)
        private String symbol;

        @Column(name = "supported", nullable = false, columnDefinition = "boolean default false")
        private boolean supported;

        @Column(name = "time_frame_in_month", nullable = false)
        private Integer timeFrameInMonth;
    }
