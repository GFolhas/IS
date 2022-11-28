package com.example;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

public class alerts implements Runnable{
    private String inputTopic;
    private String outputTopic;
    private Properties props;

    public alerts(String inputTopic, String outputTopic, Properties props){
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.props = props;
    }
    
    public void run(){

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

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
        .to(outputTopic);
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}
