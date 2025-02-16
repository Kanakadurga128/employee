package com.durga.employee.exception;

public class CustomException extends RuntimeException {
    private String message;

    // Constructor
    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
