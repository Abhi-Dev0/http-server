package com.codesmith.httpserver.controller;

import com.codesmith.httpserver.model.HttpMethod;
import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpResponse;
import com.codesmith.httpserver.route.annotation.Controller;
import com.codesmith.httpserver.route.annotation.Route;

@Controller
public class HomeController {

    @Route(path = "/", httpMethod = HttpMethod.GET)
    public HttpResponse home(HttpRequest request){
        return new HttpResponse();
    }

    @Route(path = "/", httpMethod = HttpMethod.POST)
    public HttpResponse homePost(HttpRequest request){
        return new HttpResponse();
    }

}
