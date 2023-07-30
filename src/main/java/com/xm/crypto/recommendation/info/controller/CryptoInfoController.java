package com.xm.crypto.recommendation.info.controller;

import com.xm.crypto.recommendation.common.dto.ResponseDTO;
import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.service.CryptoInfoService;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/cryptos")
public class CryptoInfoController {

    private final CryptoInfoService cryptoInfoService;

    @Autowired
    public CryptoInfoController(CryptoInfoService cryptoInfoService) {
        this.cryptoInfoService = cryptoInfoService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<ResponseDTO> getCryptoInfo(@PathVariable String symbol) {
    return Optional.ofNullable(cryptoInfoService.getCryptoInfo(symbol))
            .map(this::prepareRespectiveResponse)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto symbol not found for given symbol " + symbol));
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