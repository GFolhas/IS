package com.example;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimpleProducer {

  public static void main(String[] args) throws Exception {
    // ex1();
    // ex2();
    // ex3();
    // ex4();
    // ex5();
    // ex6();
    // ex7();
    // ex8();
    // ex9();
    // ex10();
    // ex11();
  }

  public static void ex1() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> new KeyValue<>(k, v))
        .groupByKey()
        .count()
        .mapValues(c -> c.toString())
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex2() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(vals[0], v);
        })
        .groupByKey()
        .count()
        .mapValues(c -> c.toString())
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex3() {
    ex3Max();
    ex3Min();
  }

  public static void ex3Min() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

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
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex3Max() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

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
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex4() {
    ex4Max();
    ex4Min();
  }

  public static void ex4Min() {

    final DecimalFormat df = new DecimalFormat("0.00");

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          double temperature = Double.parseDouble(vals[1]);
          temperature = temperature * 1.8;
          temperature += 32;
          String tVal = df.format(temperature).replace(",", ".");
          return new KeyValue<>(vals[0], tVal);
        })
        .selectKey((key, value) -> key)
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Double.parseDouble(value1) < Double.parseDouble(value2)) {
            return value1;
          } else {
            return value2;
          }
        })
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex4Max() {

    final DecimalFormat df = new DecimalFormat("0.00");

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          double temperature = Double.parseDouble(vals[1]);
          temperature = temperature * 1.8;
          temperature += 32;
          String tVal = df.format(temperature).replace(",", ".");
          return new KeyValue<>(vals[0], tVal);
        })
        .selectKey((key, value) -> key)
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Double.parseDouble(value1) > Double.parseDouble(value2)) {
            return value1;
          } else {
            return value2;
          }
        })
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex5() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> new KeyValue<>(k, v))
        .groupByKey()
        .count()
        .mapValues(c -> c.toString())
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex6() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(vals[1], k);
        })
        .groupByKey()
        .count()
        .mapValues(c -> c.toString())
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex7() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));
    KStream<String, String> textLines2 = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));
    textLines = textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, type)
        });

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, temp)
        }).toTable();

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.join(right, valueJoiner,
        Joined.valueSerde(Serdes.String()));

    String alert = "red";

    joined
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals); // (type, temp)
        })
        .filter((k, v) -> v[0].equals(alert))
        .map((k, v) -> new KeyValue<>(k, v[1]))
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
            return value1;
          } else {
            return value2;
          }
        })
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex8() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));
    KStream<String, String> textLines2 = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines = textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(vals[0], vals[2]);
        })
        .filter((k, v) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
          LocalDateTime dateTime = LocalDateTime.parse(v, formatter);
          LocalDateTime lasthour = LocalDateTime.now().minusHours(4);
          return lasthour.isBefore(dateTime);
        })
        .groupByKey()
        .reduce((v1, v2) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
          LocalDateTime d1 = LocalDateTime.parse(v1, formatter);
          LocalDateTime d2 = LocalDateTime.parse(v2, formatter);
          if (d1.isBefore(d2)) {
            return v1;
          } else
            return v2;
        }).toStream();

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(vals[0], vals[1]); // (location, temp)
        })
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Integer.parseInt(value1) > Integer.parseInt(value2)) {
            return value1;
          } else {
            return value2;
          }
        });

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.join(right, valueJoiner, Joined.valueSerde(Serdes.String()));

    joined
        .map((k, v) -> { // location -> station*temp
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (location, temp)
        })
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void ex9() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));
    KStream<String, String> textLines2 = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines = textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals); // (station, type)
        })
        .filter((k, v) -> v[1].equals("red"))
        .map((k, v) -> new KeyValue<>(k, v[2]))
        .groupByKey()
        .reduce((value1, value2) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
          LocalDateTime dateTime = LocalDateTime.parse(value1, formatter);
          LocalDateTime lasthour = LocalDateTime.parse(value2, formatter);

          if (lasthour.isBefore(dateTime)) {
            return value1;
          } else {
            return value2;
          }
        })
        .toStream();

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, temp)
        }).toTable();

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.join(right, valueJoiner, Joined.valueSerde(Serdes.String()));

    joined
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, temp)
        })
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
            return value1;
          } else {
            return value2;
          }
        })
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex10() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));

    textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, temp)
        })
        .groupByKey()
        .aggregate(() -> new int[] { 0, 0 }, (aggKey, newValue, aggValue) -> {
          aggValue[0] += 1;
          aggValue[1] += Integer.parseInt(newValue);

          return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .mapValues(v -> {
          if (v[0] != 0) {
            return "" + v[1] / v[0];
          } else {
            return "Divided by zero";
          }
        })
        .toStream()
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static void ex11() {

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream("alerts", Consumed.with(Serdes.String(), Serdes.String()));
    KStream<String, String> textLines2 = builder.stream("stweather", Consumed.with(Serdes.String(), Serdes.String()));
    textLines = textLines
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals); // (station, String[])
        })
        .filter((k, v) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
          LocalDateTime dateTime = LocalDateTime.parse(v[2], formatter);
          LocalDateTime lasthour = LocalDateTime.now().minusHours(12);

          return (lasthour.isBefore(dateTime) && v[1].equals("orange"));
        })
        .map((k, v) -> new KeyValue<>(k, v[0])) // (station, location)
        .groupByKey()
        .reduce((value1, value2) -> {
          return value1 + "*" + value2;
        })
        .toStream();

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]); // (station, temp)
        })
        .groupByKey()
        .aggregate(() -> new int[] { 0, 0 }, (aggKey, newValue, aggValue) -> {
          aggValue[0] += 1;
          aggValue[1] += Integer.parseInt(newValue);
          return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .mapValues(v -> {
          if (v[0] != 0) {
            return "" + v[1] / v[0];
          } else {
            return "Divided by zero";
          }
        });

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.join(right, valueJoiner, Joined.valueSerde(Serdes.String()));

    joined
        .map((k, v) -> { // station -> location*temp
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[vals.length - 1]); // (station, temp)
        })
        .to("results", Produced.with(Serdes.String(), Serdes.String()));

    String id = UUID.randomUUID().toString();
    String appId = UUID.randomUUID().toString();
    Properties props = getProperties(id, appId);

    KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.start();
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }

  public static Properties getProperties(String id, String appId) {

    Properties props = new Properties();
    // Assign localhost id

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    // props.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, Station.class);
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, id);
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    return props;
  }

}