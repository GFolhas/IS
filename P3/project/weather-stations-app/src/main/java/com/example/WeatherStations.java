package com.example;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class WeatherStations {

 public static void main(String[] args) throws Exception{
  

  String dbinfo = "dbinfo-stations";
  String stweather = "stweather";
  String alerts = "alerts";

  String id = UUID.randomUUID().toString();
  String appId = UUID.randomUUID().toString();

  java.util.Properties props = getProperties(id, appId);
  
  Gson gson = new Gson();
  

// ex 0 - DONE

// PRODUCE INFO TO STWEATHER

StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(dbinfo, Consumed.with(Serdes.String(), Serdes.String()));
Random rand = new Random();


textLines
.map((k, v) -> {
  System.out.println(v);
  JsonElement jsonElement = gson.fromJson(v, JsonElement.class);
  String name = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("name").getAsString();
  String location = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("location").getAsString();
  String random_temperature = String.valueOf(rand.nextInt(50));
  DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
  LocalDateTime now = LocalDateTime.now(); 
  String value = location + "*" + random_temperature + "*" + formater.format(now); 

  return new KeyValue<>(name, value);
})
.to(stweather, Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


// PRODUCE INFO TO ALERTS

builder = new StreamsBuilder();
textLines = builder.stream(dbinfo, Consumed.with(Serdes.String(), Serdes.String()));
String [] type = new String[]{"red", "orange", "yellow", "green"};

textLines
.map((k, v) -> {
  System.out.println(v);
  JsonElement jsonElement = gson.fromJson(v, JsonElement.class);
  String name = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("name").getAsString();
  String location = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("location").getAsString();
  int random_index = rand.nextInt(4);
  String event = type[random_index];
  DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
  LocalDateTime now = LocalDateTime.now(); 
  String value = location + "*" + event + "*" + formater.format(now); 

  return new KeyValue<>(name, value);
})
.to(alerts, Produced.with(Serdes.String(), Serdes.String()));

streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


 }


 public static Properties getProperties(String id, String appId){

    
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, id);
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    return props;
 }

}