package com.codesmith.httpserver.model;

import com.codesmith.httpserver.exception.UnsupportedHttpVersionException;

public enum HttpVersion {
    HTTP_VERSION_1_1("HTTP/1.1", 1, 1),
    HTTP_VERSION_2_0("HTTP/2.0", 2, 0);

    private String httpVersion;
    private int major;
    private int minor;

    private HttpVersion(String httpVersion, int major, int minor) {
        this.httpVersion = httpVersion;
        this.major = major;
        this.minor = minor;
    }

    public static HttpVersion getCompatibleHttpVersion(String httpVersion, int major, int minor){
        for(HttpVersion version : HttpVersion.values()){
            if(httpVersion.equals(version.httpVersion))
                return version;
            if(major == version.major && minor <= version.minor){
                return version;
            }
        }
        throw new UnsupportedHttpVersionException("Unsupported Http Version: "+httpVersion);
    }
}
