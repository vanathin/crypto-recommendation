package com.xm.crypto.recommendation.info.controller;

import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.service.CryptoStatsAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@Validated
public class CryptoInfoController {

    private final CryptoStatsAggregateService cryptoInfoService;

    @Autowired
    public CryptoInfoController(CryptoStatsAggregateService cryptoInfoService) {
        this.cryptoInfoService = cryptoInfoService;
    }

    @GetMapping("/cryptos/{symbol}/stats")
    public ResponseEntity<CryptoStatsDTO> getAggregatedCryptoStats(@PathVariable String symbol) {
        return Optional.ofNullable(cryptoInfoService.getCryptoStatsAggregationForGivenSymbol(symbol))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto symbol not found for given symbol " + symbol));
    }
}
