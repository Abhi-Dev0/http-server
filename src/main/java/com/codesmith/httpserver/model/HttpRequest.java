package com.codesmith.httpserver.model;

public class HttpRequest extends Http{

    private HttpMethod method;
    private String targetUri;


    public HttpRequest() { }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }
}
