package com.codesmith.httpserver.config;

import com.codesmith.httpserver.exception.ConfigurationException;
import com.codesmith.httpserver.util.JsonUtility;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private static ConfigManager configManager;

    private ServerConfig config;

    private ConfigManager(){}

    public static ConfigManager getInstance(){
        if(configManager == null)
            configManager = new ConfigManager();
        return configManager;
    }

    public void loadConfiguration(String filePath) throws ConfigurationException {
        try {
            logger.info("Reading configuration from file: {}", filePath);
            File jsonFile = new File(filePath);
            JsonNode node = JsonUtility.parse(jsonFile);
            this.config = JsonUtility.mapToObject(node, ServerConfig.class);
            logger.info("Loaded configuration from file: {}", filePath);
        }catch (IOException e){
            throw new ConfigurationException("Exception occurred while loading configuration from file: "+filePath,e);
        }
    }

    public ServerConfig getConfig() {
        return config;
    }
}
