package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
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
  String topicName = "messages";
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
      System.out.println("Connected to database #3");
  }


  // pull info from dbms
  


  
/*   Producer<String, String> producer = new KafkaProducer<>(props);
  Thread.sleep(1000);

  Scanner sc = new Scanner(System.in);
  while(true){
    System.out.print("Type here: ");
    String val = sc.nextLine();
    if(val.equals("end")) break;
    producer.send(new ProducerRecord<String, String>(topicName, id, val));
  } */

  //for(int i = 0; i < 1000; i++)
   //producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(i), String.valueOf(i)));
/*   
  System.out.println("Message sent successfully to topic " + topicName);
  producer.close(); */
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
