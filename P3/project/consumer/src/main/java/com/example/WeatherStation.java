package com.example;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;


public class WeatherStation {

    public static void main ( String[] args ) throws InterruptedException{
        String inputTopic = "dbinfo";
        String out1 = "stweather";
        String out2 = "alerts";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,UUID.randomUUID().toString());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

        while(true){
            textLines
            .map((name, location) -> {
                System.out.println(name);
                System.out.println(location);
                System.out.println();

                Random rand = new Random();
                int upperbound = 50;
                int int_random = rand.nextInt(upperbound);
                String temp = String.valueOf(int_random) + " degrees";
                return new KeyValue<>(location, temp);
            })
            .to(out1);
            
            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
            Thread.sleep(10000);
        }


        /* textLines
        .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
        .groupBy((key, value) -> value)
        .count()
        .mapValues((key, value) -> {
            System.out.println("mapping:");
            System.out.print(key);
            System.out.print(" : ");
            System.out.println(value);
            String tmp = String.valueOf(value) + " -> " + key;
            return tmp;
        })
        .toStream()
        .to(out1); */


        /* KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


            
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
        
        //System.out.println("Reading stream from topic " + inputTopic);

    }
}