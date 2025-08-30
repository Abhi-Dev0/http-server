package com.codesmith.httpserver.route;

import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpResponse;

@FunctionalInterface
public interface RouteHandler {

    HttpResponse handle(HttpRequest request);

}
