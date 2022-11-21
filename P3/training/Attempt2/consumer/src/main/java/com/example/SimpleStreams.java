package com.example;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;


public class SimpleStreams {

    public static void main ( String[] args ) throws InterruptedException{
        String inputTopic = "messages";
        String outtopicname = "result";


        java.util.Properties props = new Properties();
        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,UUID.randomUUID().toString());
        //props.put(StreamsConfig.CLIENT_ID_CONFIG, "wordcount-lambda-example-client");
        // Where to find Kafka broker(s).
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Specify default (de)serializers for record keys and for record values.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // Records should be flushed every 10 seconds. This is less than the default
        // in order to keep this example interactive.
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        // For illustrative purposes we disable record caches.
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // Use a temporary directory for storing state, which will be automatically removed after the test.
        //props.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());


        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

      /*   textLines.mapValues((key, value) -> {
            System.out.print(key);
            System.out.print(" : ");
            System.out.println(value);
            return value;
        })
        .groupBy((key, value) -> value)
        .count()
        .mapValues((key, value) -> {
            System.out.println("2nd mapping:");
            System.out.print(key);
            System.out.print(" : ");
            System.out.println(value);
            return String.valueOf(value);
        })
        .toStream()
        .to(outtopicname); */


        textLines
        .mapValues((key, value) -> {
            System.out.println("1st mapping:");
            System.out.print(key);
            System.out.print(" : ");
            System.out.println(value);
            return String.valueOf(value);
        })
        .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
        .groupBy((key, value) -> value)
        .count()
        .mapValues((key, value) -> {
            System.out.println("2nd mapping:");
            System.out.print(key);
            System.out.print(" : ");
            System.out.println(value);
            String tmp = String.valueOf(value) + " -> " + key;
            return tmp;
        })
        .toStream()
        .to(outtopicname);


        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


            
        /* StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(topicNamed);
        //KTable<String, Long> outlines = lines.groupByKey().count();
        KTable<String, Long> outlines = lines
           .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
           .groupBy((key, word) -> word)
           .count();

        outlines.toStream().filter((key, value) -> value != null).to(outtopicname, Produced.with(Serdes.String(), Serdes.Long()));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start(); */
        
        System.out.println("Reading stream from topic " + inputTopic);

    }
}