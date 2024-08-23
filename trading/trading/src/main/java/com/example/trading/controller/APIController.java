package com.example.trading.controller;

import com.example.trading.auth.AuthManager;
import com.example.trading.datafetcher.realtime.RealtimeClosingPriceDataFetcher;
import com.example.trading.dto.RegisterDTO;
import com.example.trading.kafka.KafkaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class APIController {

    @Autowired
    AuthManager authManager;
    @Autowired
    KafkaManager kafkaManager;
    @Autowired
    RealtimeClosingPriceDataFetcher realtimeClosingPriceDataFetcher;

    @PostMapping("/register")
    public void register(@RequestBody RegisterDTO registerDTO) {
        authManager.setAppKey(registerDTO.getAppKey());
        authManager.setAppSecret(registerDTO.getAppSecret());
        authManager.getWebSocketConnectionKey();

        kafkaManager.setBootstrap(registerDTO.getAnalyticsServer());
    }

    @GetMapping("/test")
    public String test()
    {
        realtimeClosingPriceDataFetcher.connect("005930");
        return authManager.getApprovalKey();
    }
}
