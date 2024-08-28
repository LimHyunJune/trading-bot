package com.example.trading.manager;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthManager {
    RestClient restClient;
    String appKey;
    String appSecret;
    String approvalKey;
    String accessToken;

    @PostConstruct
    public void init()
    {
        restClient = RestClient.create();
    }

    public AuthManager(String appKey, String appSecret)
    {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public void setWebSocketConnectionKey()
    {
        JSONObject jo = new JSONObject();
        try{
            jo.put("grant_type", "client_credentials");
            jo.put("appkey", appKey);
            jo.put("secretkey", appSecret);
        }
        catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            ResponseEntity<Map> response = restClient.post()
                    .uri("https://openapi.koreainvestment.com:9443/oauth2/Approval")
                    .body(jo.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Map.class);
            this.approvalKey = (String) response.getBody().get("approval_key");
        }
        catch (RestClientException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setAuthorizationKey()
    {
        JSONObject jo = new JSONObject();
        try{
            jo.put("grant_type", "client_credentials");
            jo.put("appkey", appKey);
            jo.put("appsecret", appSecret);
        }
        catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            ResponseEntity<Map> response = restClient.post()
                    .uri("https://openapi.koreainvestment.com:9443/oauth2/tokenP")
                    .body(jo.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Map.class);
            this.accessToken = (String) response.getBody().get("access_token");
        }
        catch (RestClientException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

}