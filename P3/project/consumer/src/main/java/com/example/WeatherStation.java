package com.example;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;


public class WeatherStation {

    public static void main ( String[] args ) throws InterruptedException{
        String inputTopic = "dbinfo";
        String out1 = "stweather";
        String out2 = "alerts";

        java.util.Properties props = getProperties();

        // send the standard weather events

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

        textLines
        .map((name, location) -> {

            Random rand = new Random();
            int upperbound = 50;
            int int_random = rand.nextInt(upperbound);
            String temp = String.valueOf(int_random);

            System.out.print(location);
            System.out.print(" : ");
            System.out.println(temp + " degrees");
            return new KeyValue<>(location, temp);
        })
        .to(out1);
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        /* stdWeather std = new stdWeather(inputTopic, out1, props);
        Thread t1 = new Thread(std);
        t1.start(); */

        // send the weather alerts


        builder = new StreamsBuilder();
        textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

        textLines
        .map((name, location) -> {

            String [] type = new String[]{"red", "orange", "yellow", "green"};
            Random rand = new Random();
            int upperbound = 4;
            int random_index = rand.nextInt(upperbound);

            System.out.print(location);
            System.out.print(" : ");
            System.out.println(type[random_index]);
            return new KeyValue<>(location, type[random_index]);
        })
        .to(out2);
        
        streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        /* alerts al = new alerts(inputTopic, out2, props);
        Thread t2 = new Thread(al);
        t2.start(); */

    }


    public static Properties getProperties(){
        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,UUID.randomUUID().toString());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return props;
    }
}