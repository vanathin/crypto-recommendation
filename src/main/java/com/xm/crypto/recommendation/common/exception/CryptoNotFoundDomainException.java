package com.xm.crypto.recommendation.common.exception;

import com.xm.crypto.recommendation.common.exception.DomainEntityNotFoundException;

public class CryptoNotFoundDomainException extends DomainEntityNotFoundException {
    public CryptoNotFoundDomainException(String message) {
        super(message);
    }
}
