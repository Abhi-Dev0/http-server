package com.codesmith.httpserver.handler;

import com.codesmith.httpserver.route.Router;
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
    private final Router router;

    public SocketHandler(int port, int threadPoolSize, Router router) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.router = router;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(this.port)) {
            logger.info("Started Server at port: {}", this.port);
            while(serverSocket.isBound() && !serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                executorService.execute(new RequestHandler(socket, router));
            }
        } catch (IOException e) {
            logger.error("Exception occurred while starting server at port: {}", this.port, e);
            System.exit(1);
        } catch (Exception e){
            logger.error("Exception occurred while processing request", e);
        }
        finally {
            logger.info("Stopping Server...");
            executorService.shutdown();
        }
    }

}
