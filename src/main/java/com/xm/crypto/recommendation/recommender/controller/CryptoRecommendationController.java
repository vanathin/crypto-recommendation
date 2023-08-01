package com.xm.crypto.recommendation.recommender.controller;

import com.xm.crypto.recommendation.common.exception.dto.ErrorDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoNormalizedRangeDTO;
import com.xm.crypto.recommendation.recommender.dto.CryptoRecommenderResponseDTO;
import com.xm.crypto.recommendation.recommender.service.CryptoNormalizedRangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cryptos/recommendations/")
@Tag(name = "Crypto Recommendations", description = "Endpoints related to crypto recommendations")
public class CryptoRecommendationController {

    private final CryptoNormalizedRangeService cryptoNormalizedRangeService;

    @Autowired
    public CryptoRecommendationController(CryptoNormalizedRangeService cryptoNormalizedRangeService) {
        this.cryptoNormalizedRangeService = cryptoNormalizedRangeService;
    }

    @GetMapping("highest-normalized-range")
    @Operation(
            summary = "Get crypto with highest normalized range for a specific day",
            description = "Returns the crypto with the highest normalized range for a specific day.",
            parameters = {
                    @Parameter(
                            name = "date",
                            description = "The date for which to find the crypto with the highest normalized range.",
                            required = true,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "date")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success. Returns the crypto with the highest normalized range for the given date.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CryptoRecommenderResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "204",
                            description = "No Content. If no crypto with the highest normalized range is found for the specific day."),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. If an unexpected error occurs while processing the request.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
    public ResponseEntity<CryptoRecommenderResponseDTO> getCryptoWithHighestNormalizedRange(
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Optional<CryptoRecommenderResponseDTO> result = cryptoNormalizedRangeService.findCryptoWithHighestNormalizedRange(date);
        return result
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("sorted-by-normalized-range")
    @Operation(
            summary = "Get cryptos sorted by normalized range",
            description = "Returns a list of cryptos sorted by their normalized range in descending order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success. Returns a list of cryptos sorted by their normalized range.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CryptoNormalizedRangeDTO.class)))
                    ),
                    @ApiResponse(responseCode = "204",
                            description = "No Content. If no crypto with the highest normalized range."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. If an unexpected error occurs while processing the request.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
    public ResponseEntity<List<CryptoNormalizedRangeDTO>> getCryptoNormalizedRange() {
        return Optional.ofNullable(cryptoNormalizedRangeService.findCryptoWithNormalizedRange())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
