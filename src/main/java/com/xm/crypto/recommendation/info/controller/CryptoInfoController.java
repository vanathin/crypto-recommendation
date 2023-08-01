package com.xm.crypto.recommendation.info.controller;

import com.xm.crypto.recommendation.common.exception.CryptoNotFoundDomainException;
import com.xm.crypto.recommendation.common.exception.dto.ErrorDTO;
import com.xm.crypto.recommendation.info.dto.CryptoStatsDTO;
import com.xm.crypto.recommendation.info.service.CryptoStatsAggregateService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cryptos/")
@Tag(name = "Crypto Information", description = "Endpoints related to crypto information")
public class CryptoInfoController {

    private final CryptoStatsAggregateService cryptoInfoService;

    @Autowired
    public CryptoInfoController(CryptoStatsAggregateService cryptoInfoService) {
        this.cryptoInfoService = cryptoInfoService;
    }

    @GetMapping("{symbol}/stats")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get aggregated stats for a crypto",
            description = "Returns the aggregated statistics for a specific crypto symbol.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success. Returns the aggregated statistics for the crypto symbol.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CryptoStatsDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Crypto symbol is not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. If the server encountered an unexpected condition.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
    public ResponseEntity<CryptoStatsDTO> getAggregatedCryptoStats(@PathVariable String symbol) {
        return Optional.ofNullable(cryptoInfoService.getCryptoStatsAggregationForGivenSymbol(symbol))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CryptoNotFoundDomainException("Crypto symbol is not found. " + symbol));
    }
}
