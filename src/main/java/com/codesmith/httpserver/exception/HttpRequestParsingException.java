package com.codesmith.httpserver.exception;

public class HttpRequestParsingException extends RuntimeException {
    public HttpRequestParsingException() {
    }

    public HttpRequestParsingException(String message) {
        super(message);
    }

    public HttpRequestParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestParsingException(Throwable cause) {
        super(cause);
    }
}
