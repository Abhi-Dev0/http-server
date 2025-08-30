package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.model.HttpResponse;
import com.codesmith.httpserver.route.RouteHandler;
import com.codesmith.httpserver.route.Router;
import com.codesmith.httpserver.util.RequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket socket;
    private final Router router;

    public RequestHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try(InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()){

            // Parse the HTTP Request
            HttpRequest request = RequestParser.getInstance().parseHttpRequest(in);

            // Find the appropriate handler for the request
            RouteHandler handler = router.getHandler(request.getTargetUri(), request.getMethod());

            HttpResponse response;
            if(handler != null){
                response = handler.handle(request);
            }

            String str = "Hello !, This response is from Java Http Server";
            out.write(str.getBytes());
        }catch (Exception e){
            logger.error("Exception occurred while processing request", e);
        }finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

}
