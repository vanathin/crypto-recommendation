package com.xm.crypto.recommendation.recommender.controller;

import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.recommender.service.CryptoNormalizedRangeService;
import com.xm.crypto.recommendation.recommender.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class CryptoRecommendationController {

    private final RecommenderService cryptoService;

    private final CryptoNormalizedRangeService cryptoNormalizedRangeService;

    @Autowired
    public CryptoRecommendationController(RecommenderService cryptoService, CryptoNormalizedRangeService cryptoNormalizedRangeService) {
        this.cryptoService = cryptoService;
        this.cryptoNormalizedRangeService = cryptoNormalizedRangeService;
    }

    @GetMapping("/cryptos/highest-normalized-range")
    public ResponseEntity<Optional<CryptoRecommenderResponseDTO>> getCryptoWithHighestNormalizedRange(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<CryptoRecommenderResponseDTO> result = cryptoNormalizedRangeService.findCryptoWithHighestNormalizedRange(date);

        return Optional.ofNullable(result)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto with highest normalized range not found for given date " + date));
    }

    @GetMapping("/cryptos/sorted-by-normalized-range")
    public ResponseEntity<List<CryptoNormalizedRangeDTO>> getCryptoNormalizedRange() {
        return Optional.ofNullable(cryptoNormalizedRangeService.findCryptoWithNormalizedRange())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto details not found" ));
    }

}
