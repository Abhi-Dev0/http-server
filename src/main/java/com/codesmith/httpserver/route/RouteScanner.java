package com.codesmith.httpserver.route;

import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpResponse;
import com.codesmith.httpserver.route.annotation.Controller;
import com.codesmith.httpserver.route.annotation.Route;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class RouteScanner {

    private RouteScanner() {}

    public static void scan(String basePackage, Router.Builder routerBuilder) {
        // Implementation for scanning the package and registering routes
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        // Loop in all the classes annotated with @Controller
        for(Class<?> controller : controllers){
            try {
                // Create an instance of the controller for method invocation
                Object controllerInstance = controller.getDeclaredConstructor().newInstance();

                // Loop in all the methods of the controller
                for(Method method : controller.getDeclaredMethods()){
                    if(!method.isAnnotationPresent(Route.class))
                        continue;

                    // Validate method signature
                    if(method.getParameterCount() != 1 || !method.getParameterTypes()[0].equals(HttpRequest.class))
                        throw new IllegalArgumentException("Method " + method.getName() + " in controller " + controller.getName() + " must have exactly one parameter of type HttpRequest");
                    if(!method.getReturnType().equals(HttpResponse.class))
                        throw new IllegalArgumentException("Method " + method.getName() + " in controller " + controller.getName() + " must return HttpResponse");

                    Route route = method.getAnnotation(Route.class);

                    // Register the route in the router builder
                    routerBuilder.addRoute(route.path(), route.httpMethod(), request -> {
                        try{

                            // Invoke the method on the controller instance by passing the HttpRequest
                            return (HttpResponse) method.invoke(controllerInstance, request);
                        }catch (Exception e){
                            throw new RuntimeException("Failed to invoke route handler method: " + method.getName() + " in controller: " + controller.getName(), e);
                        }
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate controller: " + controller.getName(), e);
            }
        }
    }

}
