/*
 * NOTAS SOBRE O PROJETO:
 * 
 * - Não recorremos ao uso do docker, sendo tudo realizado diretamente na
 * máquina;
 * 
 * - As execuções dos exercícios demoram um bocado mais do que o esperado,
 * principalmente nos que implicam a utilização de joins.
 * 
 * - O acesso à informação em json foi algo que nos apresentou algumas
 * dificuldades pela forma como o json estava a ser gerado, pelo que não tendo
 * muito tempo decidimos optar pela opção mais simples e rápida e assumi-mos o
 * json como string e separá-mos a informação manualmente. Não é o mais
 * eficiente mas provou-se uma solução viável.
 * 
 */

package com.example;

import java.text.DecimalFormat;
import java.util.Properties;
import java.util.UUID;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalDateTime;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
          return new KeyValue<>(k, vals[1]);
        });

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]);
        }).toTable();

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.join(right, valueJoiner,
        Joined.valueSerde(Serdes.String()));

    String alert = "red";

    joined
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals);
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
          return new KeyValue<>(vals[0], vals[1]);
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
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]);
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
          return new KeyValue<>(k, vals);
        })
        .filter((k, v) -> v[1].equals("red"))
        .map((k, v) -> new KeyValue<>(k, v[1]));

    KStream<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]);
        })
        .groupByKey()
        .reduce((value1, value2) -> {
          if (Integer.parseInt(value1) < Integer.parseInt(value2)) {
            return value1;
          } else {
            return value2;
          }
        }).toStream();

    ValueJoiner<String, String, String> valueJoiner = (l, r) -> l + "*" + r;
    Joined.keySerde(Serdes.String());
    KStream<String, String> joined = textLines.leftJoin(right, valueJoiner,
        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(30)));

    joined
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(vals[0], vals[1]);
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
          return new KeyValue<>(k, vals[1]);
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
          return new KeyValue<>(k, vals);
        })
        .filter((k, v) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
          LocalDateTime dateTime = LocalDateTime.parse(v[2], formatter);
          LocalDateTime lasthour = LocalDateTime.now().minusHours(12);

          return (lasthour.isBefore(dateTime) && v[1].equals("orange"));
        })
        .map((k, v) -> new KeyValue<>(k, v[0]))
        .groupByKey()
        .reduce((value1, value2) -> {
          return value1 + "*" + value2;
        })
        .toStream();

    KTable<String, String> right = textLines2
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[1]);
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
        .map((k, v) -> {
          String[] vals = v.split("\\*");
          return new KeyValue<>(k, vals[vals.length - 1]);
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