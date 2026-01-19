package com.ecommerce.backend.Exceptions;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
