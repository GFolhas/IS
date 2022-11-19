package com.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleConsumer {
    String bootStrap = "localhost:9092";
    String id = "app";
    String topic = "test";
    Logger logger= LoggerFactory.getLogger(SampleConsumer.class.getName());  
    
    public SampleConsumer(){
        Properties props = setProperties();
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

        kafkaConsumer.subscribe(Arrays.asList(topic));
        boolean cond = true;

        while(cond){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String,String> record: consumerRecords){  
                if(record.value().equals("end")){cond = false;}
                logger.info("Key: "+ record.key() + ", Value:" +record.value());  
                logger.info("Partition:" + record.partition()+",Offset:"+record.offset());  
            } 
        }

        //kafkaConsumer.close();
    }



    public Properties setProperties(){
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrap);  
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());  
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());  
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,id);  
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");  
        return props;
    }


}
