package com.t2404e.dishmanager.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
    super(message);
    }
}
