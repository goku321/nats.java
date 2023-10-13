package io.nats.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JSONSchema<T> implements Schema<T> {
    public ObjectMapper objectMapper;
    public Class<T> pojo;

    public JSONSchema(Class<T> pojo) {
        this.objectMapper = new ObjectMapper();
        this.pojo = pojo;
    }
    @Override
    public byte[] encode(T message) throws JsonProcessingException {
        byte[] returnBytes;
        try {
            returnBytes = objectMapper.writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            return null;
        }
        return returnBytes;
    }

    @Override
    public T decode(String subject, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, pojo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
