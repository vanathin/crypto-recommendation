package com.xm.crypto.recommendation.common.exception;

public class BlankSymbolException extends DomainEntityNotFoundException {
    public BlankSymbolException(String message) {
        super(message);
    }

    public BlankSymbolException(String message, Throwable cause) {
        super(message, cause);
    }
}
