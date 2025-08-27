package com.codesmith.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonUtility {

    private JsonUtility(){}

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode parse(File jsonFile) throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readTree(jsonFile);
    }

    public static  <T> T mapToObject(JsonNode node, Class<T> clazz) throws JsonProcessingException {
        return mapper.treeToValue(node, clazz);
    }

    public static JsonNode mapToJson(Object obj){
        return mapper.valueToTree(obj);
    }

}
