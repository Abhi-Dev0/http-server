package com.codesmith.httpserver;

import com.codesmith.httpserver.config.ConfigManager;
import com.codesmith.httpserver.config.ServerConfig;
import com.codesmith.httpserver.handler.SocketHandler;
import com.codesmith.httpserver.exception.ConfigurationException;
import com.codesmith.httpserver.route.RouteScanner;
import com.codesmith.httpserver.route.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    // Application Starting Point
    public static void main(String[] args){
        try {

            // Load Configuration from the file path
            String configFilePath = "src/main/resources/server-config.json";
            ConfigManager manager = ConfigManager.getInstance();
            manager.loadConfiguration(configFilePath);
            ServerConfig config = manager.getConfig();

            logger.info("Scanning Controllers from package: {}", config.getControllerPackage());
            // Scan and load controllers
            Router.Builder routerBuilder = new Router.Builder();
            RouteScanner.scan(config.getControllerPackage(), routerBuilder);
            Router router = routerBuilder.build();

            logger.info("Starting Server at port: {}", config.getPort());

            //Create a separate thread for socket
            Thread socketRunner = new Thread(new SocketHandler(config.getPort(), config.getThreadPoolSize(), router));
            logger.info("Creating Thread to listen socket");
            socketRunner.start();

        }catch (ConfigurationException e){
            logger.error(e.getMessage(), e);
        }

    }

}