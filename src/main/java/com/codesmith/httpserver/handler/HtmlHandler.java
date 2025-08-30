package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.model.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlHandler {

    private static HtmlHandler instance;

    private static String webroot;

    private HtmlHandler(){}

    public static HtmlHandler getInstance(){
        if(instance == null)
            instance = new HtmlHandler();
        return instance;
    }

    public void setWebrootPath(String webroot) {
        HtmlHandler.webroot = webroot;
    }

    public static HttpResponse servePage(String path) {
        if(path.startsWith("/")) path = path.substring(1);
        if(!path.endsWith(".html")) path = path + ".html";

        Path filePath = Path.of(webroot, path);

        if(!filePath.startsWith(Path.of(webroot)) || !Files.exists(filePath) || Files.isDirectory(filePath)){
            return HttpResponse.notFound("Page not found: " + path);
        }

        try{
            String html = Files.readString(filePath, StandardCharsets.UTF_8);
            return HttpResponse.ok(html, "text/html; charset=UTF-8");
        }catch (IOException e){
            return HttpResponse.internalServerError("Error reading file: " + path);
        }
    }
}
