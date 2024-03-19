package com.example.ibeproject.exceptions;

public class PropertyException extends RuntimeException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
