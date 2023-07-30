package com.xm.crypto.recommendation.common.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record ErrorDTO(String code, List<String> messages) {
}
