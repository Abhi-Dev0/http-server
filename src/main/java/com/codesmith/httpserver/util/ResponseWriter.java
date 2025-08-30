package com.codesmith.httpserver.util;

import com.codesmith.httpserver.model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseWriter {

    public static void write(OutputStream out, HttpResponse response) throws IOException {

        byte[] body = response.getBody() != null ? response.getBody().getBytes() : null;

        if(body != null)
            response.addHeader("Content-Length", String.valueOf(body.length));

        StringBuilder responseBuilder = new StringBuilder();

        // Add Status Line
        responseBuilder.append(response.getHttpVersion().getHttpVersion()).append(" ").append(response.getStatus().getCode())
                .append(" ").append(response.getStatus().getMessage()).append("\r\n");

        // Add Headers
        for(Map.Entry<String, String> header : response.getHeaders().entrySet()){
            responseBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        // End of Headers
        responseBuilder.append("\r\n");

        out.write(responseBuilder.toString().getBytes(StandardCharsets.UTF_8));
        if(body != null)
            out.write(body);
        out.flush();
    }
}
