package com.example.ibeproject.exceptions;

public class NightlyRateParsingException extends RuntimeException {

    public NightlyRateParsingException(String message) {
        super(message);
    }

    public NightlyRateParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
