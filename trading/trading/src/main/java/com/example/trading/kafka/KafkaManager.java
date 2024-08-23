package com.example.trading.kafka;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class KafkaManager {
    String bootstrap;
}
