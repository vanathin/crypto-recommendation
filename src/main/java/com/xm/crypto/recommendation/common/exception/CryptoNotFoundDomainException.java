package com.xm.crypto.recommendation.common.exception;

public class CryptoNotFoundDomainException extends DomainEntityNotFoundException {
    public CryptoNotFoundDomainException(String message) {
        super(message);
    }
}
