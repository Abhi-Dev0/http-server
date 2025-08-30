package com.codesmith.httpserver.exception;

public class UnsupportedHttpVersionException extends RuntimeException{
    public UnsupportedHttpVersionException() {
    }

    public UnsupportedHttpVersionException(String message) {
        super(message);
    }

    public UnsupportedHttpVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedHttpVersionException(Throwable cause) {
        super(cause);
    }
}
