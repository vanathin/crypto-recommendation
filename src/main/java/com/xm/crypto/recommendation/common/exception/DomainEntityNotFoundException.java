package com.xm.crypto.recommendation.common.exception;

public class DomainEntityNotFoundException extends DomainException {
    public DomainEntityNotFoundException(String message) {
        super(message);
    }

    public DomainEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
