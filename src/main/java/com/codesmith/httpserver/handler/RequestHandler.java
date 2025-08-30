package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.model.HttpRequest;
import com.codesmith.httpserver.util.RequestParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable{

    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            // Get Input and Output from the Socket
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // Parse the HTTP Request
            HttpRequest request = RequestParser.getInstance().parseHttpRequest(in);

            Thread.sleep(5000);
            String str = "Hello !, This response is from Java Http Server";
            out.write(str.getBytes());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
