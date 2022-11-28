package com.example;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

public class stdWeather implements Runnable{
    private String inputTopic;
    private String outputTopic;
    private Properties props;

    public stdWeather(String inputTopic, String outputTopic, Properties props){
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.props = props;
    }
    
    public void run(){

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
        .to(outputTopic);
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}
