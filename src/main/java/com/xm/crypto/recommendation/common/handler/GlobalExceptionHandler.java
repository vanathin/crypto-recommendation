package com.xm.crypto.recommendation.common.handler;


import com.xm.crypto.recommendation.common.dto.ErrorDTO;
import com.xm.crypto.recommendation.common.exception.DomainEntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String CLIENT_ERROR = "client_side_error";


    @ExceptionHandler(DomainEntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorDTO notFoundErrorHandler(final DomainEntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(List.of(exception.getMessage()))
                .build();
    }
}