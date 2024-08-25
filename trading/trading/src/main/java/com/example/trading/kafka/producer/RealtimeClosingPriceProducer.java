package com.example.trading.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RealtimeClosingPriceProducer {
    Properties props;
    KafkaProducer<String, String> producer;

    private final String topic = "closing_price";

    public RealtimeClosingPriceProducer(){
        props = new Properties();
    }

    public void init(String bootstrapServer)
    {
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void send(String key, String value)
    {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic,key,value);
        producer.send(record);
        producer.flush();
    }
}
