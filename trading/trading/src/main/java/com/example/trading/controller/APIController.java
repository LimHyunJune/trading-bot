package com.example.trading.controller;

import com.example.trading.manager.AuthManager;
import com.example.trading.datafetcher.realtime.RealtimeClosingPriceDataFetcher;
import com.example.trading.dto.RegisterDTO;
import com.example.trading.manager.KafkaManager;
import com.example.trading.model.Buying;
import com.example.trading.model.Selection;
import com.example.trading.model.Selling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class APIController {

    Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    AuthManager authManager;
    @Autowired
    KafkaManager kafkaManager;
    @Autowired
    Selection selection;
    @Autowired
    Buying buying;
    @Autowired
    Selling selling;

    @PostMapping("/register")
    public void register(@RequestBody RegisterDTO registerDTO) {
        logger.info(String.valueOf(registerDTO));
        authManager.setAppKey(registerDTO.getAppKey());
        authManager.setAppSecret(registerDTO.getAppSecret());

        if(registerDTO.getApprovalKey().isEmpty())
            authManager.setWebSocketConnectionKey();
        else
            authManager.setApprovalKey(registerDTO.getApprovalKey());

        if(registerDTO.getAccessToken().isEmpty())
            authManager.setAuthorizationKey();
        else
            authManager.setAccessToken(registerDTO.getAccessToken());

        //kafkaManager.init(registerDTO.getAnalyticsServer());
    }

    /*
     * 자동 매매 시 /select, /buy, /sell 통합 가능
     */
    @GetMapping("/select")
    public String select()
    {
        if(selection.isEmpty())
            selection.start();
        return  selection.getFirstStock();
    }

    @GetMapping("/buy")
    public void buy(@RequestParam("stock") String stock)
    {
        buying.start(stock);
        buying.complete();
    }

    @GetMapping("/sell")
    public void sell()
    {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}