package com.example.trading.datafetcher.realtime;

import com.example.trading.auth.AuthManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@ClientEndpoint
public class RealtimeClosingPriceDataFetcher {

    @Autowired
    AuthManager authManager;

    @Autowired
    RealTimeDataParser realTimeDataParser;

    private Session session;
    private WebSocketContainer container;
    private final String uri = "ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0";

    // 종목 코드
    private String trKey;

    public RealtimeClosingPriceDataFetcher()
    {
        container = ContainerProvider.getWebSocketContainer();
    }

    public void connect(String trKey)
    {
        System.out.println("RealtimeClosingPriceDataFetcher Connecting to " + uri);
        this.trKey = trKey;
        try {
            container.connectToServer(this, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
        sendMessage(trKey);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
        if(message.charAt(0) == '0') realTimeDataParser.parse(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error occurred: " + throwable.getMessage());
    }

    public void sendMessage(String trKey) {
        Map<String,Object> header = new HashMap<String,Object>();
        header.put("approval_key", authManager.getApprovalKey());
        header.put("custtype", "P");
        header.put("tr_type", "1");
        header.put("content-type", "utf-8");

        Map<String, Object> input = new HashMap<String, Object>();
        input.put("tr_id", "H0STCNT0");
        input.put("tr_key", trKey);

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("input", input);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("header", header);
        data.put("body", body);

        String sendJson = null;
        try {
            sendJson = new ObjectMapper().writeValueAsString(data);
            System.out.println("[Send JsonString] : " + sendJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.session.getAsyncRemote().sendText(sendJson);
    }


}
