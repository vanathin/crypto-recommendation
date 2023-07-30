package com.xm.crypto.recommendation.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ResponseDTO {

    private boolean status;
    private Object data;
}
