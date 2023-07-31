package com.xm.crypto.recommendation.recommender.controller;

import com.xm.crypto.recommendation.common.dto.ResponseDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.recommender.service.CryptoNormalizedRangeService;
import com.xm.crypto.recommendation.recommender.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cryptos")
public class CryptoRecommendationController {

    private final RecommenderService cryptoService;

    private final CryptoNormalizedRangeService cryptoNormalizedRangeService;

    @Autowired
    public CryptoRecommendationController(RecommenderService cryptoService, CryptoNormalizedRangeService cryptoNormalizedRangeService) {
        this.cryptoService = cryptoService;
        this.cryptoNormalizedRangeService = cryptoNormalizedRangeService;
    }

    @GetMapping(value = "/highest-normalized-range", produces = "application/json")
    public ResponseEntity<ResponseDTO> getCryptoWithHighestNormalizedRange(
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return Optional.ofNullable(cryptoService.findByDay(date))
                .map(this::prepareRespectiveResponse)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto with highest normalized range not found for given date " + date));
    }
    private ResponseEntity<ResponseDTO> prepareRespectiveResponse(CryptoRecommenderResponseDTO cryptoNormalizedRangeResDTO) {
        if (cryptoNormalizedRangeResDTO != null) {
            return ResponseEntity.ok(ResponseDTO.builder().data(cryptoNormalizedRangeResDTO)
                    .status(true)
                    .build());
        } else {
            return ResponseEntity.accepted().body(ResponseDTO.builder().status(false).build());
        }
    }

    @GetMapping("/normalized-range")
    public ResponseEntity<ResponseDTO> getCryptoNormalizedRange() {
        return Optional.ofNullable(cryptoNormalizedRangeService.findCryptoWithNormalizedRange())
                .map(this::prepareRespectiveResponseForNormalizedRange)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto details not found" ));
    }

    private ResponseEntity<ResponseDTO> prepareRespectiveResponseForNormalizedRange(List<CryptoNormalizedRangeDTO> cryptoNormalizedRangeDTO) {
        if (cryptoNormalizedRangeDTO != null) {
            return ResponseEntity.ok(ResponseDTO.builder().data(cryptoNormalizedRangeDTO)
                    .status(true)
                    .build());
        } else {
            return ResponseEntity.accepted().body(ResponseDTO.builder().status(false).build());
        }
    }
}
