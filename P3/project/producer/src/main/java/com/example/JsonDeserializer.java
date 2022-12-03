package com.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Class<T> classname;
    public static final String KEY_CLASS_NAME_CONFIG = "key.class.name";
    public static final String VALUE_CLASS_NAME_CONFIG = "value.class.name";

    // default constructor needed by Kafka
    public JsonDeserializer() {}

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        // nothing to do
        if(isKey)
            classname = (Class<T>) props.get(KEY_CLASS_NAME_CONFIG);
        else
            classname = (Class<T>) props.get(VALUE_CLASS_NAME_CONFIG);
    }

    public T deserialize(String topic, byte[] data){
        if(data == null){
            return null;
        }
        try {
            return objectMapper.readValue(data, classname);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializationException();
        }
    }

    @Override
    public void close() {
        // nothing to do
    }

}