package com.codesmith.httpserver.model;

import com.codesmith.httpserver.util.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class HttpResponse extends Http{

    private HttpStatus status;

    public HttpResponse() {
        this.setHttpVersion(HttpVersion.HTTP_VERSION_1_1);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public static HttpResponse ok(String body, String contentType) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setBody(body);
        response.addHeader("Content-Type", contentType);
        return response;
    }

    public static HttpResponse jsonOk(Object obj) throws JsonProcessingException {
        JsonNode node = JsonUtility.mapToJson(obj);
        String body = JsonUtility.stringify(node);
        return ok(body, "application/json");
    }

    public static HttpResponse notFound(String message) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody(message);
        response.addHeader("Content-Type", "text/plain");
        return response;
    }

    public static HttpResponse internalServerError(String message) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(message);
        response.addHeader("Content-Type", "text/plain");
        return response;
    }

    public static HttpResponse badRequest(String message) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setBody(message);
        response.addHeader("Content-Type", "text/plain");
        return response;
    }
}
