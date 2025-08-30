package com.codesmith.httpserver.route.annotation;

import com.codesmith.httpserver.model.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Make this annotation available at runtime
@Target(ElementType.METHOD)     // This annotation can be applied to methods
public @interface Route {
    String path();
    HttpMethod httpMethod();
    String produces() default "application/json";
}
