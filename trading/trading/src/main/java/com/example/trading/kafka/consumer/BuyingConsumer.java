package com.example.trading.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

@Component
public class BuyingConsumer {
    Logger logger = LoggerFactory.getLogger(BuyingConsumer.class);
    Properties props;
    KafkaConsumer<String, String> consumer;

    private final String topic = "buying";

    public BuyingConsumer(){
        props = new Properties();
    }

    public void init(String bootstrapServer)
    {
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    }

    public void poll()
    {
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Pattern.compile(topic));

        Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("main program starts to exit by calling wakeup");
            consumer.wakeup();
            try {
                mainThread.join();
            } catch(InterruptedException e) { e.printStackTrace();}
        }));

        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            if(!records.isEmpty())
            {
                consumer.close();
                break;
            }
        }
    }
}