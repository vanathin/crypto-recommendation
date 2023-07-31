package com.xm.crypto.recommendation.info.controller;

import com.xm.crypto.recommendation.common.dto.ResponseDTO;
import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.info.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.service.CryptoStatsAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cryptos")
public class CryptoInfoController {

    private final CryptoStatsAggregateService cryptoInfoService;

    @Autowired
    public CryptoInfoController(CryptoStatsAggregateService cryptoInfoService) {
        this.cryptoInfoService = cryptoInfoService;
    }

    @GetMapping("/stats/{symbol}")
    public ResponseEntity<ResponseDTO> getAggregatedCryptoStats(@PathVariable String symbol) {
        return Optional.ofNullable(cryptoInfoService.getCryptoStatsAggregationForGivenSymbol(symbol))
                .map(this::prepareRespectiveResponse)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto symbol not found for given symbol " + symbol));
    }

    @GetMapping("/normalized-range")
    public ResponseEntity<ResponseDTO> getCryptoNormalizedRange() {
        return Optional.ofNullable(cryptoInfoService.findCryptoWithNormalizedRange())
                .map(this::prepareRespectiveResponseForNormalizedRange)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto not found" ));
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

    private ResponseEntity<ResponseDTO> prepareRespectiveResponse(CryptoStatsDTO cryptoStatsDTO) {
        if (cryptoStatsDTO != null) {
            return ResponseEntity.ok(ResponseDTO.builder().data(cryptoStatsDTO)
                    .status(true)
                    .build());
        } else {
            return ResponseEntity.accepted().body(ResponseDTO.builder().status(false).build());
        }
    }
}
