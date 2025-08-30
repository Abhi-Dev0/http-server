package com.codesmith.httpserver.model;

import java.util.HashMap;
import java.util.Map;

public class Http {

    private String body;
    private final Map<String, String> headers = new HashMap<>();

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }
}
