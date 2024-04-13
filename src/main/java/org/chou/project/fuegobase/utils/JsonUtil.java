package org.chou.project.fuegobase.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonUtil {

    private final static ObjectMapper objectMapper =
            new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonUtil() {

    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Occur error during parsing data to json: {}", obj, e);
            return "";
        }
    }

    public static <T> T toObject(String json, Class<T> objectClass) {
        try {
            return objectMapper.readValue(json, objectClass);
        } catch (IOException e) {
            log.error("Occur error during mapping json to an object: {}", json, e);
            return null;
        }
    }

}
