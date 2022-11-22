package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;

public class SimpleProducer {

 public static void main(String[] args) throws Exception{

  //Assign topicName to string variable
  String topicName = "dbinfo";
  String id = UUID.randomUUID().toString();
  Properties props = getProperties(id);


  // connect to dbms
  // Connect method #3
  String dbURL = "jdbc:postgresql://localhost:5432/kafka";
  Properties parameters = new Properties();
  parameters.put("user", "postgres");
  parameters.put("password", "postgres");

  Connection conn = DriverManager.getConnection(dbURL, parameters);
  if (conn != null) {
      System.out.println("Connected to database");
  }

  // pull info from dbms

  ArrayList<Station> allStations = new ArrayList<>();
  Statement stmt = conn.createStatement();
  ResultSet rs = stmt.executeQuery( "select * from stations;" );

  while ( rs.next() ) {
    int sid = rs.getInt("id");
    String  name = rs.getString("name");
    String location  = rs.getString("location");
    System.out.printf( "Id = %s\nName = %s\nLocation = %s\n\n", sid, name, location);
    Station st = new Station(sid, name, location);
    allStations.add(st);
  }

  rs.close();
  stmt.close();
  conn.close();


  Producer<String, String> producer = new KafkaProducer<>(props);

  for(int i = 0; i < allStations.size(); i++)
    producer.send(new ProducerRecord<String, String>(topicName,allStations.get(i).getName(), allStations.get(i).getLocation()));

  System.out.println("Message sent successfully to topic " + topicName);
  producer.close();
 }


 public static Properties getProperties(String id){

    
    Properties props = new Properties();
    //Assign localhost id
    props.put("bootstrap.servers", "localhost:9092");

    props.put(StreamsConfig.CLIENT_ID_CONFIG, id);

    //Set acknowledgements for producer requests.      
    props.put("acks", "all");

    //If the request fails, the producer can automatically retry,
    props.put("retries", 0);

    //Specify buffer size in config
    props.put("batch.size", 16384);

    //Reduce the no of requests less than 0   
    props.put("linger.ms", 1);

    //The buffer.memory controls the total amount of memory available to the producer for buffering.   
    props.put("buffer.memory", 33554432);

    props.put("key.serializer", 
        "org.apache.kafka.common.serialization.StringSerializer");

    props.put("value.serializer", 
        "org.apache.kafka.common.serialization.StringSerializer");

    return props;
 }

}
