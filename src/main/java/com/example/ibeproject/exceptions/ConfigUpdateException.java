package com.example.ibeproject.exceptions;

public class ConfigUpdateException extends RuntimeException {

    public ConfigUpdateException(String message) {
        super(message);
    }

    public ConfigUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
