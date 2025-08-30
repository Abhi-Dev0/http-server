package com.codesmith.httpserver.model;

import com.codesmith.httpserver.exception.UnSupportedHttpMethodException;

public enum HttpMethod {
    GET, POST;

    public static HttpMethod getHttpMethod(String method){
        try {
            return HttpMethod.valueOf(method);
        }catch (IllegalArgumentException e){
            throw new UnSupportedHttpMethodException("Unsupported Http Method: "+method);
        }
    }
}
