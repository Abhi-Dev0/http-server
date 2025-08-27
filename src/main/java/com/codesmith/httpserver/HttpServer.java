package com.codesmith.httpserver;

import com.codesmith.httpserver.config.ConfigManager;
import com.codesmith.httpserver.config.ServerConfig;
import com.codesmith.httpserver.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

            logger.info("Strating Server at port: {}", config.getPort());

        }catch (ConfigurationException e){
            logger.error(e.getMessage(), e);
        }

    }

}