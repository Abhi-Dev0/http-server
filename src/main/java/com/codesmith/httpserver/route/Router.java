package com.codesmith.httpserver.route;

import com.codesmith.httpserver.handler.RouteHandler;
import com.codesmith.httpserver.model.HttpMethod;

import java.util.*;

public class Router {

    private final Map<String, Map<HttpMethod, RouteHandler>> routes;

    private Router(Map<String, Map<HttpMethod, RouteHandler>> routes) {
        this.routes = routes;
    }

    public RouteHandler getHandler(String uriPath, HttpMethod method) {
        String path = normalizePath(uriPath);
        Map<HttpMethod, RouteHandler> routeHandler = routes.get(path);
        return (routeHandler == null) ? null : routeHandler.get(method);
    }

    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    // Builder class for constructing Router instances
    public static class Builder {
        private final Map<String, Map<HttpMethod, RouteHandler>> innerRoutes = new HashMap<>();

        public void addRoute(String uriPath, HttpMethod method, RouteHandler handler) {
            Objects.requireNonNull(uriPath);
            Objects.requireNonNull(method);
            Objects.requireNonNull(handler);

            String path = normalizePath(uriPath);

            // Get or create the method-to-handler map for the given path
            Map<HttpMethod, RouteHandler> route = innerRoutes.computeIfAbsent(path, k -> new EnumMap<>(HttpMethod.class));

            if (route.containsKey(method)) {
                throw new IllegalArgumentException("Route already exists for path: " + path + " and method: " + method);
            }
            route.put(method, handler);
        }

        public Router build() {
            Map<String, Map<HttpMethod, RouteHandler>> outerRoutes = new HashMap<>();
            for (var entry : innerRoutes.entrySet()) {
                // Create an unmodifiable copy of each inner map
                outerRoutes.put(entry.getKey(), Collections.unmodifiableMap(new EnumMap<>(entry.getValue())));
            }

            // Create an unmodifiable copy of the outer map
            return new Router(Collections.unmodifiableMap(outerRoutes));
        }
    }
}
