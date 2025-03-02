package com.droplite.exception;

public class DropLiteException extends RuntimeException {

    public DropLiteException(String message) {
        super(message);
    }

    public DropLiteException(String message, Exception e) {
        super(message, e);
    }
}
