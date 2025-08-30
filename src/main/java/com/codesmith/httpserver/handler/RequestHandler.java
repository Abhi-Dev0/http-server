package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.model.*;
import com.codesmith.httpserver.route.Router;
import com.codesmith.httpserver.util.RequestParser;
import com.codesmith.httpserver.util.ResponseWriter;
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
        InputStream in = null;
        OutputStream out = null;
        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();

            // Parse the HTTP Request
            HttpRequest request = RequestParser.getInstance().parseHttpRequest(in);

            if(request.getMethod() == HttpMethod.OPTIONS){
                HttpResponse response = new HttpResponse();
                response.setStatus(HttpStatus.NO_CONTENT);// No Content
                response.setHttpVersion(HttpVersion.HTTP_VERSION_1_1);
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type");
                ResponseWriter.write(out, response);
                return;
            }

            // Find the appropriate handler for the request
            RouteHandler handler = router.getHandler(request.getTargetUri(), request.getMethod());


            HttpResponse response;
            if(handler != null){
                // Handle the request and generate response
                response = handler.handle(request, handler.getRoute());
            }else{
                // No handler found for the request
                response = HttpResponse.notFound("No handler found for "+request.getMethod()+" "+request.getTargetUri());
            }

            //Set Http Version in response
            response.setHttpVersion(request.getHttpVersion());

            // Write the HTTP Response
            ResponseWriter.write(out, response);

        }catch (Exception e){
            logger.error("Exception occurred while processing request", e);
            try {
                ResponseWriter.write(out, HttpResponse.internalServerError("Exception occurred while processing request"));
            } catch (IOException ignored) {}
        }finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException ignored) {}
        }
    }

}
