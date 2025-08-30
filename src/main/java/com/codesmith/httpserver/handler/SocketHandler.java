package com.codesmith.httpserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private final int port;
    private final ExecutorService executorService;

    public SocketHandler(int port, int threadPoolSize) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(this.port)) {
            logger.info("Started Server at port: {}", this.port);
            while(serverSocket.isBound() && !serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                executorService.execute(new RequestHandler(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            logger.info("Stopping Server...");
            executorService.shutdown();
        }
    }

}
