package com.codesmith.httpserver.exception;

public class UnSupportedHttpMethodException extends RuntimeException{
    public UnSupportedHttpMethodException() {
    }

    public UnSupportedHttpMethodException(String message) {
        super(message);
    }

    public UnSupportedHttpMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportedHttpMethodException(Throwable cause) {
        super(cause);
    }
}
