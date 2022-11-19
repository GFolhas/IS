package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleProducer {
    Logger logger= LoggerFactory.getLogger(SampleProducer.class.getName());  
    String topic = "test";
    String key = "name";
    String value = "Folhas";
    
    public SampleProducer(){
        Properties props = setProperties();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, value);
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        kafkaProducer.send(producerRecord);
        //logger.info("Key: "+ key + ", Value:" + value + ", Topic: " + topic);
        kafkaProducer.close();
    }


    public Properties setProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

}
