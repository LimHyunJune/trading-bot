package com.example.trading.manager;

import com.example.trading.kafka.producer.RealtimeClosingPriceProducer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class KafkaManager {
    String bootstrapServer;

    @Autowired
    RealtimeClosingPriceProducer realtimeClosingPriceProducer;

    public void init(String bootstrapServer)
    {
        this.bootstrapServer = bootstrapServer;
        realtimeClosingPriceProducer.init(bootstrapServer);
    }
}