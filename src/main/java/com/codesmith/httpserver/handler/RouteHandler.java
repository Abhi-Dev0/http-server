package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpResponse;
import com.codesmith.httpserver.route.annotation.Route;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(RouteHandler.class);

    private final Object controller;
    private final Method method;
    private final Route route;

    public RouteHandler(Object controller, Method method, Route route) {
        this.controller = controller;
        this.method = method;
        this.route = route;
    }

    public HttpResponse handle(HttpRequest request, Route route) throws Exception {
        Object response = null;
        try{
            // Invoke the method on the controller instance by passing the HttpRequest
            response = method.invoke(controller, request);

            if(response instanceof String strResponse){
                if("text/html".equalsIgnoreCase(route.produces()))
                    response = HtmlHandler.servePage(strResponse);
                else
                    response = HttpResponse.ok(strResponse, "text/plain; charset=UTF-8");
            }else
                response = HttpResponse.jsonOk(response);
        }catch (JsonProcessingException e){
            response = HttpResponse.badRequest("Invalid JSON in request body");
            logger.error("JSON processing error in route handler method: {} in controller: {}", method.getName(), controller.getClass(), e);
        }
        catch (InvocationTargetException | IllegalArgumentException e){
            response = HttpResponse.internalServerError("Internal server error");
            logger.error("Error invoking route handler method: {} in controller: {}", method.getName(), controller.getClass(), e);
        }catch (Exception e){
            response = HttpResponse.internalServerError("Internal server error");
            logger.error("Exception occurred in route handler method: {} in controller: {}", method.getName(), controller.getClass(), e);
        }
        return (HttpResponse) response;
    }

    public Route getRoute(){
        return this.route;
    }

}
