package com.t2404e.dishmanager.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
    super(message);
    }
}
