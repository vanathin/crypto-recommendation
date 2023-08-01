package com.xm.crypto.recommendation.common.exception.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
public class ErrorDTO {

    private String code;
    private List<String> messages;

    public ErrorDTO(String code, List<String> messages) {
        this.code = code;
        this.messages = messages;
    }
}
