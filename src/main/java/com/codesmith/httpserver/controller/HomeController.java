package com.codesmith.httpserver.controller;

import com.codesmith.httpserver.model.HttpMethod;
import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.User;
import com.codesmith.httpserver.route.annotation.Controller;
import com.codesmith.httpserver.route.annotation.Route;
import com.codesmith.httpserver.util.JsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class HomeController {

    @Route(path = "/", httpMethod = HttpMethod.GET, produces = "text/html")
    public String root(HttpRequest request){
        return "index.html";
    }

    @Route(path = "/greeting", httpMethod = HttpMethod.GET, produces = "text/plain")
    public String greeting(HttpRequest request) throws JsonProcessingException {
        return "Hello, User!";
    }

    @Route(path = "/user", httpMethod = HttpMethod.GET)
    public User home(HttpRequest request){
        return new User("John", "Doe", "john123@mail.cpm");
    }

    @Route(path = "/user", httpMethod = HttpMethod.POST)
    public User user(HttpRequest request) throws JsonProcessingException {
        JsonNode node = JsonUtility.parse(request.getBody());
        return JsonUtility.mapToObject(node, User.class);
    }

}
