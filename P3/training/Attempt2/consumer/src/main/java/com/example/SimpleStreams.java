package com.example;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;


public class SimpleStreams {

    public static void main ( String[] args ) throws InterruptedException{
        String topicNamed = "messages";
        String outtopicname = "result";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
            
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Long> lines = builder.stream(topicNamed);

        KTable<String, Long> outlines = lines.groupByKey().count();

        outlines.toStream().filter((key, value) -> value != null).to(outtopicname);
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("Reading stream from topic " + topicNamed);

    }
}