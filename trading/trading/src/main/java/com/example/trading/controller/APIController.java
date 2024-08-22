package com.example.trading.controller;

import com.example.trading.auth.AuthDTO;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
public class APIController {

    @PostMapping("/register")
    public void register(@RequestBody AuthDTO authDTO) throws JSONException {
        RestClient restClient = RestClient.create();
        JSONObject jo = new JSONObject();
        jo.put("grant_type", "client_credentials");
        jo.put("appkey", authDTO.getAppKey());
        jo.put("secretkey", authDTO.getAppSecret());


        ResponseEntity<Map> response = restClient.post()
                .uri("http://localhost:8080/example")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jo)
                .retrieve()
                .toEntity(Map.class);

        System.out.println("body = " + response.getBody());
    }
}
