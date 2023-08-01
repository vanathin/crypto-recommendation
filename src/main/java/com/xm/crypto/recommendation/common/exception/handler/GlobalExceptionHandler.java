package com.xm.crypto.recommendation.common.exception.handler;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.xm.crypto.recommendation.common.exception.DomainEntityNotFoundException;
import com.xm.crypto.recommendation.common.exception.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String CLIENT_ERROR = "client_side_error";

    public static final String SERVER_ERROR = "server_side_error";

    @ExceptionHandler({DomainEntityNotFoundException.class,})
    @ResponseStatus(NOT_FOUND)
    ErrorDTO notFoundErrorHandler(final DomainEntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(List.of(exception.getMessage()))
                .build();
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ErrorDTO internalServerErrorHandler(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(SERVER_ERROR)
                .messages(List.of(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .build();
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO parameterMissingExceptionErrorHandler(final MissingServletRequestParameterException exception) {
        final List<String> errorMessages;
        String paramValue = "Request param is empty";
        if (exception.getCause() instanceof JsonMappingException error) {
            errorMessages = error.getPath()
                    .stream()
                    .map(reference -> String.format("%s: %s",
                            reference.getFieldName(),
                            error.getOriginalMessage()))
                    .toList();

            log.error("{}", errorMessages, error);
        } else {
            if (exception.getCause() != null) {
                errorMessages = List.of(exception.getCause().getMessage());
            } else {
                errorMessages = List.of(paramValue);
            }
            log.error("{}", errorMessages, exception.getCause());
        }

        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(errorMessages)
                .build();
    }

}