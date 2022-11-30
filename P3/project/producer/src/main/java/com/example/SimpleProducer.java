package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

public class SimpleProducer {

 public static void main(String[] args) throws Exception{

  final Logger log = LoggerFactory.getLogger(SimpleProducer.class);
  final DecimalFormat df = new DecimalFormat("0.00");
  


  String inputTopic = "stations";
  String outputTopic1 = "stweather";
  String outputTopic2 = "alerts";
  String id = UUID.randomUUID().toString();
  String appId = UUID.randomUUID().toString();

  java.util.Properties props = getProperties(id, appId);


/*   StreamsBuilder builder = new StreamsBuilder();
  KStream<String, String> textLines = builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
  
  textLines.peek((key, value) -> {
    System.out.println("key: " + key);
    System.out.println("value: " + value);
  })
  .filter((key, value) -> key.equals(null)); */


  // PRODUCING GENERAL INFO TO START THE REQUIREMENTS

/*   ArrayList<Station> allStations = new ArrayList<>();
  KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
  // subscribe consumer to our topic(s)
  consumer.subscribe(Arrays.asList(inputTopic)); */

/*   while(true){
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

    for (ConsumerRecord<String, String> record : records){

        String[] x = record.value().split("payload\"");

        x[1] = x[1].replace(":", " ");
        x[1] = x[1].replace("{", "");
        x[1] = x[1].replace("}", "");
        x[1] = x[1].replace("\"", "");

        String[] nstr = x[1].split(",");

        String eventId = nstr[0].split(" ")[2];
        String station = nstr[1].split(" ")[1];
        String location = nstr[2].split(" ")[1];
        
        allStations.add(new Station(Integer.parseInt(eventId), station, location));
    }

    if(allStations.size() == 8) break;
} */


// ex 1 - DONE (takes a while to run)


/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> new KeyValue<>(k, v))
.groupByKey()
.count()
.mapValues(c -> c.toString())
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


// ex 2 - DONE (takes a while to run)

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  return new KeyValue<>(vals[0], v);
})
.groupByKey()
.count()
.mapValues(c -> c.toString())
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */



// ex 3 - DONE (takes a while to run)

//Max temp per weather station

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  return new KeyValue<>(k, vals[1]);
})
.selectKey((key, value) -> key)
.groupByKey()
.reduce((value1, value2) -> {
  if (Integer.parseInt(value1) > Integer.parseInt(value2)) {
      return value1;
  } else {
      return value2;
  }
})
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


//Min temp per weather station

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  return new KeyValue<>(k, vals[1]);
})
.selectKey((key, value) -> key)
.groupByKey()
.reduce((value1, value2) -> {
  if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
      return value1;
  } else {
      return value2;
  }
})
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


// ex 4 - DONE (takes a while to run)

//Max temp per location

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  double temperature = Double.parseDouble(vals[1]);
  temperature = temperature*1.8;
  temperature += 32;
  return new KeyValue<>(vals[0], df.format(temperature));
})
.selectKey((key, value) -> key)
.groupByKey()
.reduce((value1, value2) -> {
  if (Integer.parseInt(value1) > Integer.parseInt(value2)) {
      return value1;
  } else {
      return value2;
  }
})
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


//Min temp per location

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  double temperature = Double.parseDouble(vals[1]);
  temperature = temperature*1.8;
  temperature += 32;
  return new KeyValue<>(vals[0], df.format(temperature));
})
.selectKey((key, value) -> key)
.groupByKey()
.reduce((value1, value2) -> {
  if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
      return value1;
  } else {
      return value2;
  }
})
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


// ex 5 - DONE (takes a while to run)


/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic2, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> new KeyValue<>(k, v))
.groupByKey()
.count()
.mapValues(c -> c.toString())
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


// ex 6 - DONE (takes a while to run)

/* StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic2, Consumed.with(Serdes.String(), Serdes.String()));

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  return new KeyValue<>(vals[1], k);
})
.groupByKey()
.count()
.mapValues(c -> c.toString())
.toStream()
.to("testing", Produced.with(Serdes.String(), Serdes.String()));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */


// ex 7 

StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = builder.stream(outputTopic2, Consumed.with(Serdes.String(), Serdes.String()));
StreamsBuilder builder2 = new StreamsBuilder();
KStream<String, String> textLines2 = builder2.stream(outputTopic1, Consumed.with(Serdes.String(), Serdes.String()));

String alert = "green";

textLines
.map((k, v) -> {
  String[] vals = v.split("\\*");
  return new KeyValue<>(vals[1], k); // (type, station)
})
.map((k, v) -> {

  KStream<String, String> smallest = textLines2
  .map((k2, v2) -> {
    String[] vals = v2.split("\\*");
    return new KeyValue<>(k, vals[1]); // (station, temp)
  })
  .filter((k2, v2) -> k.equals(alert) && k2.equals(v))
  .selectKey((key, value) -> key)
  .groupByKey()
  .reduce((value1, value2) -> {
    if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
        return value1;
    } else {
        return value2;
    }
  })
  .toStream();

  // basicamente o que falta é pegar no value2 e tornar isso o nosso v (abaixo)

  return new KeyValue<>(k, v);
})
.groupByKey();

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
Runtime.getRuntime().addShutdownHook(new Thread(streams::close));












// PRODUCE STUFF FOR STATIONS

/*   Producer<String, String> producer = new KafkaProducer<>(props);
  producer.send(new ProducerRecord<String, String>(inputTopic, "A Station", "Agueda"));
  producer.send(new ProducerRecord<String, String>(inputTopic, "A Station", "Fermentelos"));
  producer.send(new ProducerRecord<String, String>(inputTopic, "B Station", "Condeixa"));
  producer.send(new ProducerRecord<String, String>(inputTopic, "C Station", "Arroais"));
  producer.close(); */

/*   ArrayList<Station> allStations = new ArrayList<>();
  allStations.add(new Station("A Station", "Agueda"));
  allStations.add(new Station("A Station", "Fermentelos"));
  allStations.add(new Station("B Station", "Condeixa"));
  allStations.add(new Station("C Station", "Arroais")); */

// PRODUCE STUFF FOR STWEATHER


/*   Producer<String, String> producer = new KafkaProducer<>(props);
  Random rand = new Random();  
  int upperbound = 50;

  for(int i = 0; i < allStations.size(); i++){
    int int_random = rand.nextInt(upperbound);
    String temp = String.valueOf(int_random);
    producer.send(new ProducerRecord<String, String>(outputTopic1, allStations.get(i).getName(), allStations.get(i).getLocation() + "*" + temp));
  }

  producer.close(); */




  // PRODUCE STUFF FOR ALERTS

/*   Producer<String, String> producer = new KafkaProducer<>(props);
  Random rand = new Random();  
  int upperbound = 4;
  String [] type = new String[]{"red", "orange", "yellow", "green"};
  
  
  for(int i = 0; i < allStations.size(); i++){
    int random_index = rand.nextInt(upperbound);
    String event = type[random_index];
    producer.send(new ProducerRecord<String, String>(outputTopic2, allStations.get(i).getName(), allStations.get(i).getLocation() + "*" + event));
  }

  producer.close(); */



  // =================================================




  /* textLines
  .map((name, loc) -> {

      Random rand = new Random();
      int upperbound = 50;
      int int_random = rand.nextInt(upperbound);
      String temp = String.valueOf(int_random);

      System.out.print(loc);
      System.out.print(" : ");
      System.out.println(temp + " degrees");
      return new KeyValue<>(loc, temp);
  })
  .to(outputTopic1); */
  
  /* KafkaStreams streams = new KafkaStreams(builder.build(), props);
  streams.start();
  Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); */



   // send the standard weather events
/* 
  // create consumer
  KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
  // subscribe consumer to our topic(s)
  consumer.subscribe(Arrays.asList(inputTopic));

  while(true){
    ConsumerRecords<String, String> records =
            consumer.poll(Duration.ofMillis(100));

    for (ConsumerRecord<String, String> record : records){

        String[] x = record.value().split("payload\"");

        x[1] = x[1].replace(":", " ");
        x[1] = x[1].replace("{", "");
        x[1] = x[1].replace("}", "");
        x[1] = x[1].replace("\"", "");

        String[] nstr = x[1].split(",");

        String eventId = nstr[0].split(" ")[2];
        String station = nstr[1].split(" ")[1];
        String location = nstr[2].split(" ")[1];
        
        log.info("\n\n");
        log.info(eventId);
        log.info(station);
        log.info(location + "\n\n");

    }
} */


//TODO: work this info and send it to two different topics




/* 
  //Assign topicName to string variable
  String topicName = "dbinfo";
  String id = UUID.randomUUID().toString();
  Properties props = getProperties(id);


  // connect to dbms
  
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

  while(true){
    Thread.sleep(1000); // generate new values every 15 seconds
    for(int i = 0; i < allStations.size(); i++)
    producer.send(new ProducerRecord<String, String>(topicName,allStations.get(i).getName(), allStations.get(i).getLocation()));
  }
   */
  //System.out.println("Message sent successfully to topic " + topicName);
  //producer.close();


 }


 public static Properties getProperties(String id, String appId){

    
    Properties props = new Properties();
    //Assign localhost id
    
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




//! (°C x 9/5) + 32 =°F
/*
ex4 --> {
  - mesma logica do ex 2
  - em vez de se fazer o .count()
  - tentar fazer sort
  - ir buscar o 1.o / ultimo
  - converter para ºF
}

ex5 --> {
  - fazer a leitura dos dados do tópico do alerts
  - fazer groupByKey (para agrupar por nome da estação)
  - fazer count()
  - colocar o resultado no topico results
}

ex6 --> {
  - fazer leitura do topico alerts
  - fazer filter pelo tipo de alert
  - fazer count()
  - escrever o resultado para o tópico de results
}

ex7 --> {
  - ler info alerts
  - fazer filtro por red alerts --> r1
  - ler info do stdweather
  - fazer filtro pela key do r1 --> r2
  - calcular o minimo dos value (temp) do r2
}

ex8 --> {
  - ler info alerts
  - fazer filtro por ocorrencia da ultima hora --> r1
  - ler info do stdweather
  - fazer filtro pela value (location) do r1 --> r2
  - fazer groupByValue (location)
  - calcular o máximo dos value (temp) do r2
}

ex9 --> {
  
}




*/ 
